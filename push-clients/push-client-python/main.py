from socket_factory import *
from bio_client import *
from auto_confirm_message_handler import *
from connector_factory import *
import atexit
import time
def handler(msg):
  print (msg)

def onClose():
  client.close()
  time.sleep(12)


client = BioClient('9527', socketFactory=SocketFactory())
client.messageHandler = AutoConfirmMessageHandler(callback=handler, client=client)
connector = newConnector('http://192.168.1.100:30001')
client.connect(connector)

print ('client up')

atexit.register(onClose)