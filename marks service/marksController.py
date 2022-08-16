from flask import Flask, request

from marksService import Marks_service

app = Flask(__name__)

service = Marks_service()


# Получение оценок, тело запроса должно содержать email и пароль для того, чтобы спарсить
@app.route('/api/marks', methods=['GET'])
def get_all_events_by_group_name():
    json = request.get_json()
    username = json['username']
    password = json['password']
    return service.get_marks(password=password, username=username)


if __name__ == "__main__":
    app.run(port=8084)
