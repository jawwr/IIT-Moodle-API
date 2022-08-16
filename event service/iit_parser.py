import json
from datetime import datetime

from selenium import webdriver
from selenium.webdriver.common.by import By


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


class Parser_IIT_csu(Parser):
    url = 'https://eu.iit.csu.ru/login/'

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

    # Метод парсинга событий с сайта
    def parse_event(self, password, username) -> list[dict[str]]:
        self.entry(password=password, username=username)

        calendar_text = self.browser.find_element(By.TAG_NAME, 'body') \
            .find_element(By.CLASS_NAME, 'calendarwrapper') \
            .find_element(By.TAG_NAME, 'tbody').text

        self.browser.close()

        return convert_event_to_json(calendar_text)


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
