import json

import pika
import psycopg2
from selenium import webdriver
from selenium.webdriver.common.by import By

from config import *

conn = psycopg2.connect(host=db_host, database=db_dataBase, user=db_username, password=db_password)
cur = conn.cursor()


class UserCredentials:
    login: str
    password: str

    def __init__(self, login: str, password: str):
        self.login = login
        self.password = password

    def __init__(self, json_body):
        self.__dict__ = json.loads(json_body)


class User:
    group_name: str
    name: str
    surname: str
    login: str
    password: str


class Parser:
    options = webdriver.ChromeOptions()
    options.add_argument('--incognito')
    options.add_argument('--headless')
    options.add_argument('--disable-extensions')
    options.add_argument('start-maximized')
    options.add_argument('disable-infobars')

    browser: webdriver

    username: str
    user_password: str

    def __init__(self, username, password):
        self.username = username
        self.password = password

    def entry(self) -> None:
        pass


class Parser_IIT_csu(Parser):
    url = 'https://eu.iit.csu.ru'

    def entry(self) -> None:
        self.browser = webdriver.Chrome(chrome_options=self.options)
        self.browser.get(self.url)

        xpath_username = '//*[@id="username"]'
        xpath_password = '//*[@id="password"]'

        self.browser.find_element(By.XPATH, xpath_username).send_keys(self.username)
        self.browser.find_element(By.XPATH, xpath_password).send_keys(self.password)

        entry_btn = self.browser.find_element(By.ID, 'loginbtn')
        entry_btn.click()

    def parse_user_detail(self) -> User:
        self.entry()

        user = User()

        user_detail_btn_xpath = '//*[@id="action-menu-1-menu"]/a[2]'

        btn = self.browser.find_element(By.XPATH, user_detail_btn_xpath)
        detail_href = btn.get_property('href')

        self.browser.get(detail_href)

        user_full_name = self.browser.find_element(By.CLASS_NAME, 'page-header-headings').text.split()
        user_group = self.browser.find_element(By.XPATH,
                                               '//*[@id="region-main"]/div/div/div/section[1]/div/ul/li[6]/dl/dd').text

        user.name = user_full_name[1]
        user.surname = user_full_name[0]
        user.group_name = user_group
        user.login = self.username
        user.password = self.password

        self.browser.close()

        return user


def parse(login: str, password: str):
    try:
        parser = Parser_IIT_csu(username=login,
                                password=password)

        user = parser.parse_user_detail()
        save(user=user)
    except:
        print("Wrong credentials")


def save(user: User):
    id = get_last_id()
    sql = "INSERT INTO users(id, group_name, login, name, password, surname) VALUES (%(id)s,%(group)s, %(login)s, %(name)s, %(password)s ,%(surname)s)"
    cur.execute(sql,
                {"id": id, "group": user.group_name, "name": user.name, "surname": user.surname, "login": user.login,
                 "password": user.password})
    conn.commit()


def get_last_id() -> int:
    sql = "SELECT MAX(id) FROM users"
    cur.execute(sql)
    id = cur.fetchall()
    if id == [(None,)]:
        return 1
    return int(id)


def callback(ch, method, properties, body):
    print("message")
    credentials = UserCredentials(body.decode("utf-8"))
    parse(login=credentials.login, password=credentials.password)


def main():
    parameters = pika.ConnectionParameters()
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()
    channel.queue_declare(queue='myQueue', durable=True)
    channel.basic_consume(queue='myQueue', auto_ack=True, on_message_callback=callback)
    channel.start_consuming()


if __name__ == '__main__':
    main()