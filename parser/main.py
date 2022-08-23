import json
from datetime import datetime
import pika

from selenium import webdriver
from selenium.webdriver.common.by import By


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
def convert_event_to_json(text) -> list:
    text_lines = text.split('\n')
    list_events = []
    for i in range(0, len(text_lines) - 1):
        if i % 2 == 0:
            continue
        if text_lines[i + 1].__contains__('No events'):
            continue
        list_events.append(Event(
            date=text_lines[i],
            event_name=text_lines[i + 1],
            lesson_name=''))
    return list_events


def convert_marks_to_json(marks: dict) -> list:
    list_marks = []
    for i in marks.keys():
        mark = Mark(name=i, mark=marks[i])
        list_marks.append(mark.__dict__)

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
    def parse_event(self, password, username) -> list[dict[str]]:
        self.entry(password=password, username=username)

        calendar_text = self.browser.find_element(By.TAG_NAME, 'body') \
            .find_element(By.CLASS_NAME, 'calendarwrapper') \
            .find_element(By.TAG_NAME, 'tbody').text

        self.browser.close()

        return convert_event_to_json(calendar_text)
        # Парсинг оценок

    def parse_marks(self, password, username):
        self.entry(username=username, password=password)

        self.browser.get('https://eu.iit.csu.ru/grade/report/overview/index.php')

        list_marks = {}

        table = self.browser.find_element(By.XPATH, '//*[@id="overview-grade"]')

        course_name = list(map(lambda x: x.text, table.find_elements(By.TAG_NAME, 'a')))

        course_marks = list(map(lambda x: x.text, table.find_elements(By.CLASS_NAME, 'cell.c1')))

        self.browser.close()

        for i in range(0, len(course_name)):
            list_marks.update({course_name[i]: course_marks[i]})

        return convert_marks_to_json(list_marks)

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


class Event:
    event_name: str
    date: str
    lesson_name: str

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4, ensure_ascii=False)

    def __init__(self, event_name: str,
                 date: datetime,
                 lesson_name: str):
        self.eventName = event_name
        self.date = date.strftime("%Y-%m-%d")
        self.lesson_name = lesson_name


def callback(ch, method, properties, body):
    send_events()


def parse_user_info():#TODO разделить по разным программам для асинхронности
    pass


def parse_events():
    connection = pika.BlockingConnection()
    channel = connection.channel()
    channel.queue_declare(queue='eventQueue', durable=True)
    channel.basic_consume(queue='eventQueue', auto_ack=True, on_message_callback=callback)
    channel.start_consuming()


def send_events():
    list_events = [{"eventName": "что-то 1", "lessonName": "какой-то", "date": datetime.now().__str__()},
                   {"eventName": "что-то 2", "lessonName": "какой-то", "date": datetime.now().__str__()},
                   {"eventName": "что-то 3", "lessonName": "какой-то", "date": datetime.now().__str__()}]
    body = json.dumps(list_events)
    connection = pika.BlockingConnection()
    channel = connection.channel()
    # channel.exchange_declare(exchange='', durable=True)
    channel.queue_declare(queue='myQueue', durable=True)
    channel.basic_publish(exchange="", routing_key='myQueue', body=body)
    # connection.close()


def parse_marks():
    pass


if __name__ == '__main__':
    parse_events()
