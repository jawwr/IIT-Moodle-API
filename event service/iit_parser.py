import json
from datetime import datetime
from typing import Dict

from selenium import webdriver
from selenium.webdriver.common.by import By


class Parser:
    options = webdriver.ChromeOptions()
    options.add_argument('--incognito')
    options.add_argument('--headless')
    options.add_argument('--disable-extensions')
    options.add_argument('start-maximized')
    options.add_argument('disable-infobars')

    browser = webdriver.Chrome(chrome_options=options)

    def entry(self, username, password) -> None:
        pass


def convert_event_to_json(text) -> str:
    text_lines = text.split('\n')
    list_events = []
    for i in range(0, len(text_lines) - 1):
        if i % 2 == 0:
            continue
        list_events.append(Event(date=text_lines[i], name=text_lines[i + 1]).__dict__)
    return list_events


def convert_marks_to_json(marks: dict) -> list:
    list_marks = []
    for i in marks.keys():
        mark = Mark(name=i, mark=marks[i])
        list_marks.append(mark.__dict__)

    return list_marks


class Parser_IIT_csu(Parser):
    url = 'https://eu.iit.csu.ru/login/'

    def entry(self, username, password) -> None:
        self.browser.get(self.url)

        xpath_username = '//*[@id="username"]'
        xpath_password = '//*[@id="password"]'

        self.browser.find_element(By.XPATH, xpath_username).send_keys(username)
        self.browser.find_element(By.XPATH, xpath_password).send_keys(password)

        entry_btn = self.browser.find_element(By.ID, 'loginbtn')
        entry_btn.click()

    def parse_event(self, password, username) -> str:
        self.entry(password=password, username=username)

        calendar_text = self.browser.find_element(By.TAG_NAME, 'body') \
            .find_element(By.CLASS_NAME, 'calendarwrapper') \
            .find_element(By.TAG_NAME, 'tbody').text

        self.browser.close()

        return convert_event_to_json(calendar_text)

    def parse_marks(self, password, username) -> Dict:
        self.entry(username=username, password=password)

        self.browser.get('https://eu.iit.csu.ru/grade/report/overview/index.php')

        list_marks = {}

        table = self.browser.find_element(By.XPATH, '//*[@id="overview-grade"]')

        course_name = list(map(lambda x: x.text, table.find_elements(By.TAG_NAME, 'a')))

        course_marks = list(map(lambda x: x.text, table.find_elements(By.CLASS_NAME, 'cell.c1')))

        self.browser.close()

        for i in range(0, len(course_name)):
            list_marks.update({course_name[i]: course_marks[i]})

        return list_marks


class Event:
    event_name: str
    date: str
    lesson_name: str

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4, ensure_ascii=False)

    def __init__(self, event_name: str, date: datetime, lesson_name: str):
        self.eventName = event_name
        self.date = date.strftime("%Y-%m-%d")
        self.lesson_name = lesson_name


class Mark:
    name: str
    mark: str

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False, indent=4, ensure_ascii=False)

    def __init__(self, name: str, mark):
        self.name = name
        self.date = mark
