from flask import Flask, request
from eventService import Event_service

app = Flask(__name__)

service = Event_service()


# TODO сделать проверку на то, есть ли события с нужной группой, если нет, то спарсить
# TODO реализовать парсинг событий по расписанию
@app.route('/api/events', methods=['GET'])
def get_all_events_by_group_name():
    group_name = request.args['group']
    return service.get_event_by_group_name(group_name=group_name)
    # json = request.get_json()
    # username = json['username']
    # password = json['password'] #будут нужны для парсинга


if __name__ == "__main__":
    app.run()
