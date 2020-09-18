
import socket
from bio_client import *
from socket_channel import *
import time

class BioThreadIoManager:
  def __init__(self, s: socket, client: BioClient):
    self.client = client
    self.socketChannel = SocketChannel(s)
    self.running = False
  
  def send(self, msg):
    self.socketChannel.writeAndFlush()
  
  def shutdown(self):
    self.running = False
    self.socketChannel.close()

  def startThread(self):
    self.running = True
  
  def __heartbeat__(self):
    while self.running:
      self.send('heartbeat-' + self.client.userId)
      time.sleep(10000)
    print ('heartbeat stop!')
  
  def __ioloop__(self):
    while self.running:
      str = self.socketChannel.readLine()
      self.client.onMessage(str)