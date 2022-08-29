import json
import threading
from datetime import datetime
from typing import List

import pika
from selenium import webdriver
from selenium.webdriver.common.by import By


class Event:
    eventName: str
    date: str
    lessonName: str
    groupName: str

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4, ensure_ascii=False)

    def __init__(self, event_name: str,
                 date: datetime,
                 lesson_name: str,
                 group_name: str):
        self.eventName = event_name
        self.date = date.strftime("%Y-%m-%d")
        self.lessonName = lesson_name
        self.groupName = group_name


class Mark:
    lessonName: str
    mark: str

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False, indent=4, ensure_ascii=False)

    def __init__(self, name: str, mark):
        self.lessonName = name
        self.mark = mark


class UserCredentials:
    login: str
    password: str
    group: str

    def __init__(self, login: str, password: str, group: str):
        self.login = login
        self.password = password
        self.group = group

    def __init__(self, json_body: str):
        self.__dict__ = json.loads(json_body)


class User:
    groupName: str
    name: str
    surname: str
    login: str
    password: str

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False, indent=4, ensure_ascii=False)


class Parser:
    # Настройки парсера
    options = webdriver.ChromeOptions()
    options.add_argument('--incognito')
    options.add_argument('--headless')
    options.add_argument('--disable-extensions')
    options.add_argument('start-maximized')
    options.add_argument('disable-infobars')

    browser: webdriver

    def entry(self, username, password) -> None:
        pass


# Метод для конвертации спаршенных событий в лист объектов
def convert_event_to_json(text) -> list[str]:
    text_lines = text.split('\n')
    list_events = list()
    for i in range(0, len(text_lines) - 1):
        if i % 2 == 0:
            continue
        if text_lines[i + 1].__contains__('No events'):
            continue
        list_events.append(Event(
            date=text_lines[i],
            event_name=text_lines[i + 1],
            lesson_name='').toJSON())
    return list_events


def convert_marks_to_json(marks: dict) -> list[Mark]:
    list_marks: list[Mark] = list[Mark]()
    for i in marks.keys():
        mark = Mark(name=i, mark=marks[i])
        list_marks.append(mark.toJSON())

    return list_marks


class Parser_IIT_csu(Parser):
    url = 'https://eu.iit.csu.ru/login/'
    username: str
    password: str

    # Вход на сайт
    def entry(self, username, password) -> None:
        self.browser = webdriver.Chrome(chrome_options=self.options)
        self.browser.get(self.url)

        xpath_username = '//*[@id="username"]'
        xpath_password = '//*[@id="password"]'

        self.browser.find_element(By.XPATH, xpath_username).send_keys(username)
        self.browser.find_element(By.XPATH, xpath_password).send_keys(password)

        entry_btn = self.browser.find_element(By.ID, 'loginbtn')
        entry_btn.click()
        self.username = username
        self.password = password

    # Метод парсинга событий с сайта
    def parse_event(self, password, username) -> list[str]:
        self.entry(password=password, username=username)

        calendar_text = self.browser.find_element(By.TAG_NAME, 'body') \
            .find_element(By.CLASS_NAME, 'calendarwrapper') \
            .find_element(By.TAG_NAME, 'tbody').text

        self.browser.close()

        return convert_event_to_json(calendar_text)
        # Парсинг оценок

    def parse_marks(self, password, username) -> list[Mark]:
        self.entry(username=username, password=password)

        self.browser.get('https://eu.iit.csu.ru/grade/report/overview/index.php')

        list_marks = dict()

        table = self.browser.find_element(By.XPATH, '//*[@id="overview-grade"]')

        course_name = list(map(lambda x: x.text, table.find_elements(By.TAG_NAME, 'a')))

        course_marks = list(map(lambda x: x.text, table.find_elements(By.CLASS_NAME, 'cell.c1')))

        self.browser.close()

        for i in range(0, len(course_name)):
            list_marks.update({course_name[i]: course_marks[i]})

        return convert_marks_to_json(list_marks)

    def parse_user_detail(self, username: str, password: str) -> User:
        self.entry(username=username, password=password)

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
        user.groupName = user_group
        user.login = self.username
        user.password = self.password

        self.browser.close()

        return user


def send_message(message: str, queue: str, routing_key: str):
    connection = pika.BlockingConnection()
    channel = connection.channel()
    channel.queue_declare(queue=queue, durable=True)
    channel.basic_publish(exchange="", routing_key=queue, body=message)
    print(f'message was sent to {queue}')
    connection.close()


def parse_user_info(credentials: str):
    user_credentials = UserCredentials(credentials)
    parser = Parser_IIT_csu()
    user_info = parser.parse_user_detail(password=user_credentials.password, username=user_credentials.login)
    user_info = user_info.toJSON()
    send_message(message=user_info, queue='userQueueService', routing_key='user_service_key')


def parse_events(credentials: str):
    user_credentials = UserCredentials(credentials)
    parser = Parser_IIT_csu()
    events = parser.parse_event(password=user_credentials.password, username=user_credentials.login)
    for event in events:
        event.group_name = user_credentials.group
    events = json.dumps(events)
    send_message(message=events, queue='eventQueue', routing_key='event_service_key')


def parse_marks(credentials: str):
    user_credentials = UserCredentials(credentials)
    parser = Parser_IIT_csu()
    marks = parser.parse_marks(password=user_credentials.password, username=user_credentials.login)
    marks = json.dumps(marks)
    send_message(message=marks, queue='marksQueue', routing_key='marks_service_key')


class ConsumerThread(threading.Thread):
    def __init__(self, queue: str, exchange: str, routing_key: str, *args, **kwargs):
        super(ConsumerThread, self).__init__(*args, **kwargs)

        self._queue = queue
        self._exchange = exchange
        self._routing_key = routing_key

    def callback(self, ch, method, properties, body):
        if method.exchange == 'event_parser_exchange':
            parse_events(body.decode('utf-8'))
        elif method.exchange == 'user_parser_exchange':
            parse_user_info(body.decode('utf-8'))
        elif method.exchange == 'marks_parser_exchange':
            parse_marks(body.decode('utf-8'))
        else:
            connection = pika.BlockingConnection()
            channel = connection.channel()
            channel.queue_declare(queue="eventQueue", durable=True)
            channel.basic_publish(exchange='', routing_key="event_service_key", body=body)
            connection.close()

    def run(self) -> None:
        pass
        print(f"queue {self._queue} start")
        connection = pika.BlockingConnection()
        channel = connection.channel()

        channel.exchange_declare(exchange=self._exchange, exchange_type='topic')
        channel.queue_declare(queue=self._queue, durable=True)

        channel.queue_bind(queue=self._queue, exchange=self._exchange, routing_key=self._routing_key)
        channel.basic_consume(queue=self._queue, auto_ack=True, on_message_callback=self.callback)
        channel.start_consuming()


if __name__ == '__main__':
    consumers = [
        ConsumerThread(queue='eventQueueParser', exchange='event_parser_exchange', routing_key='event_parser_key'),
        ConsumerThread(queue='userQueueParser', exchange='user_parser_exchange', routing_key='user_parser_key'),
        ConsumerThread(queue='marksQueueParser', exchange='marks_parser_exchange', routing_key='marks_parser_key')
        ]
    for thread in consumers:
        thread.start()
