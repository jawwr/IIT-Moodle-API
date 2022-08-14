import psycopg2

from config import *


def group_name_converter(group_name: str) -> str:
    if group_name.__contains__('ПрИ'):
        return group_name.replace('ПрИ', 'PrE')
    else:
        return group_name.replace('БИ', 'BI')


class Event_Repository:
    conn = psycopg2.connect(host=db_host, database=db_dataBase, user=db_username, password=db_password, )
    cur = conn.cursor()

    # Получение всех событий по названию группы
    def get_event_by_group_name(self, group_name: str):
        group_name = group_name_converter(group_name=group_name)
        self.cur.execute("SELECT * FROM events WHERE group_name = %(group)s", {'group': group_name})
        response = self.cur.fetchall()
        return response
