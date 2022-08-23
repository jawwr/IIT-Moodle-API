import py_eureka_client
from flask import Flask, request
from eventService import Event_service
from apscheduler.schedulers.background import BackgroundScheduler

# eureka_client.init(eureka_server="http://localhost:8761/eureka",
#                    app_name="data-aggregation-service",
#                    instance_port=8050)

app = Flask(__name__)

service = Event_service()


# Получение расписания по названию группы
# Если в базе данных нет событий, то испульзуются данные из тела запроса для того, чтобы спарсить данные
# TODO изменить тело запроса после написания gateway
@app.route('/api/events', methods=['GET'])
def get_all_events_by_group_name():
    group_name = request.args['group']
    all_events = service.get_event_by_group_name(group_name=group_name)
    if len(all_events) != 0:
        return all_events
    json = request.get_json()
    username = json['username']
    password = json['password']
    return service.parse_events(password=password, username=username, group=group_name)


if __name__ == "__main__":
    scheduler = BackgroundScheduler(daemon=True)
    scheduler.add_job(service.parse_events_by_schedule, 'interval', hours=12)
    scheduler.start()
    app.run(port=8085)


