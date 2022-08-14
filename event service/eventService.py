from iit_parser import *
from eventRepository import *


def parse_events(events: list) -> list[dict[str]]:
    list_events = []
    for elem in events:
        list_events.append(Event(event_name=elem[2], date=elem[3], lesson_name=elem[4]).__dict__)
    return list_events


class Event_service:
    parser = Parser_IIT_csu()
    repository = Event_Repository()

    def get_event_by_group_name(self, group_name: str):
        response = self.repository.get_event_by_group_name(group_name=group_name)
        return parse_events(response)
        # return self.parser.parse_event(password=password, username=username) #Будет потом нужно
