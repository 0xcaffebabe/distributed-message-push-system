from socket_factory import *
from socket_channel import *
from bio_thread_io_manager import *
from bio_client import *
from auto_confirm_message_handler import *
from connector_factory import *

def handler(msg):
  print('i got '+msg)

client = BioClient('9527', socketFactory=SocketFactory())
client.messageHandler = AutoConfirmMessageHandler(callback=handler, client=client)
connector = newConnector('http://192.168.1.100:30001')

client.connect(connector)

print ('client up')
