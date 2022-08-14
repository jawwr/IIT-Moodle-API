from iit_parser import *
from eventRepository import *


class Event_service:
    parser = Parser_IIT_csu()
    repository = Event_Repository()

    def get_event(self, password, username) -> str:
        return self.parser.parse_event(password=password, username=username)
