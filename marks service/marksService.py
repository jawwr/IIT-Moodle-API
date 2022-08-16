from marksParser import *


class Marks_service:
    parser = Parser_IIT_csu()

    # Метод парсинга всех последних событий если в базе нет доступных
    def parse_events(self, password: str, username: str, group: str):
        pass

    def get_marks(self, password: str, username: str):
        marks = self.parser.parse_marks(password=password, username=username)
        return marks
