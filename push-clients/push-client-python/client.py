# -*- coding: utf-8 -*-

class Client:

    def __init__(self, messageHandler):
        self.messageHandler = messageHandler

    def onMessage(self, msg):
        if self.messageHandler is not None:
            self.messageHandler.handle(msg)
