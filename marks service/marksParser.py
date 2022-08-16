import json

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


def convert_marks_to_json(marks: dict) -> list:
    list_marks = []
    for i in marks.keys():
        mark = Mark(name=i, mark=marks[i])
        list_marks.append(mark.__dict__)

    return list_marks


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


class Mark:
    lessonName: str
    mark: str

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False, indent=4, ensure_ascii=False)

    def __init__(self, name: str, mark):
        self.lessonName = name
        self.mark = mark
