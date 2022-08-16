from flask import Flask, request
from eventService import Event_service
from apscheduler.schedulers.background import BackgroundScheduler

app = Flask(__name__)

service = Event_service()


# Получение расписания по названию группы, тело запроса пока что должно содержать email и пароль для того, чтобы спарсить
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
    app.run(port=8083)
