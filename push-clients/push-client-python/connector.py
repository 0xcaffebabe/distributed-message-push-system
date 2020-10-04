# -*- coding: utf-8 -*-
from http_template import *
import requests

class Connector:
    def __init__(self, lookup_address):
        self.lookup_address = lookup_address
        self.host = ''
        self.port = 0

    def lookupConnector(self):
        response = requests.get(self.lookup_address).text
        splitResult = response.split(':')
        if len(splitResult) != 2:
            raise IOError('获取 connector 失败')
        self.host = splitResult[0]
        self.port = int(splitResult[1])

    def isAvailable(self):
        return self.host != '' and self.port > 0
