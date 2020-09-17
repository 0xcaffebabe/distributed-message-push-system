# -*- coding: utf-8 -*-

class Client:

    def __init__(self, messageHandler):
        self.messageHandler = messageHandler

    def connect(self, connector):
        ...

    def send(self, msg):
        ...

    def onMessage(self, msg):
        if self.messageHandler is None:
            self.messageHandler.handle(msg)

    def close(self):
        ...
