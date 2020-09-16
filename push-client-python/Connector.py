from http_template import *
import requests

class Connector:
    def __init__(self, lookup_address):
        self.lookup_address = lookup_address
        self.host = ''
        self.port = 0

    def lookup_connector(self):
        response = requests.get(self.lookup_address).text
        splitResult = response.split(':')
        if len(splitResult) != 2:
            raise IOError('获取 connector 失败')
        self.host = splitResult[0]
        self.port = int(splitResult[1])

    def is_available(self):
        return self.host != '' and self.port > 0


connector = Connector('http://192.168.1.100:30001')
print (connector.is_available())
connector.lookup_connector()
print (connector.is_available())
print(connector.host)
print(connector.port)