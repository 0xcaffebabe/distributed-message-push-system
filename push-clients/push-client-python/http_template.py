import requests

class HttpTemplate:
    def __init__(self):
        ...
    def get(self, url):
        r = requests.get(url)
        return r.text
