
class Client:

    def __init__(self, message_handler):
        self.message_handler = message_handler

    def connect(self, connector):
        ...

    def send(self, msg):
        ...

    def on_message(self, msg):
        if self.message_handler is None:
            self.message_handler.handle(msg)

    def close(self):
        ...
