
import socket
from bio_client import *
from socket_channel import *
import time
import threading

class BioThreadIoManager:
  def __init__(self, s: socket, client: BioClient):
    self.client = client
    self.socketChannel = SocketChannel(s)
    self.running = False
  
  def send(self, msg):
    self.socketChannel.writeAndFlush(msg)
  
  def shutdown(self):
    self.running = False
    self.socketChannel.close()

  def startThread(self):
    self.running = True

    self.heartbeatThread = threading.Thread(target=self.__heartbeat__)
    self.ioloopThread = threading.Thread(target=self.__ioloop__)
    self.heartbeatThread.start()
    self.ioloopThread.start()
  
  def __heartbeat__(self):
    while self.running:
      self.send('heartbeat-' + self.client.userId)
      time.sleep(10)
    print ('heartbeat stop!')
  
  def __ioloop__(self):
    while self.running:
      str = self.socketChannel.readLine()
      print ('receive:' + str)
      self.client.onMessage(str)
    print ("ioloop thread stop!")