from iit_parser import *
from eventRepository import *


def parse_events_to_json(events: list) -> list[dict[str]]:
    list_events = []
    for elem in events:
        list_events.append(Event(event_name=elem[2], date=elem[3], lesson_name=elem[4]).__dict__)
    return list_events


class Event_service:
    parser = Parser_IIT_csu()
    repository = Event_Repository()

    # Метод получения всех событий по названию группы
    def get_event_by_group_name(self, group_name: str):
        response = self.repository.get_event_by_group_name(group_name=group_name)
        return parse_events_to_json(response)

    # Метод парсинга всех последних событий если в базе нет доступных
    def parse_events(self, password: str, username: str, group: str):
        events = self.parser.parse_event(password=password, username=username)
        self.repository.set_event_to_db(events=events, group=group)
        return events

    # Метод парсинга событий по расписанию
    def parse_events_by_schedule(self):
        pass  # TODO доделать после написания сервиса с аккаунтами
