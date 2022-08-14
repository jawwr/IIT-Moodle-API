from flask import Flask
from eventService import *
import psycopg2
from flask_sqlalchemy import SQLAlchemy
from config import *

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgres://dbqtppqecsavvq:aa470cb280984a432ac0d641fe10f087319fd6dd06690f73f675732514ae9e4d@ec2-54-228-125-183.eu-west-1.compute.amazonaws.com:5432/d6pqj3uak52stl'
db = SQLAlchemy(app)


class Event(db.Model):
    name = ''
    date = ''

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False, indent=4, ensure_ascii=False)

    def __init__(self, name, date):
        self.name = name
        self.date = date


class Mark:
    name = ''
    mark = ''

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False, indent=4, ensure_ascii=False)

    def __init__(self, name, mark):
        self.name = name
        self.date = mark



conn = psycopg2.connect(host=db_host, database=db_dataBase, user=db_username, password=db_password,)
cur = conn.cursor()
service = Event_service()


@app.route('/test1', methods=['GET'])
def testMethod():
    print(cur.execute("SELECT * FROM events;"))
    # return service.get_event(username='kachesov03@mail.ru', password='Senya2003')
    return 'ok'


@app.route('/test2', methods=['GET'])
def testMethod1():
    return 'test'


if __name__ == "__main__":
    app.run()
