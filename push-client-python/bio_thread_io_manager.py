
import socket
from bio_client import *
from socket_channel import *
import time
import threading

class BioThreadIoManager:
  def __init__(self, s: socket, client):
    self.client = client
    self.socketChannel = SocketChannel(s)
    self.running = False
  
  def send(self, msg):
    self.socketChannel.writeAndFlush(msg)
  
  def close(self):
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
      try:
        self.send('heartbeat-' + self.client.userId)
      except:
        print('发送心跳失败')
      time.sleep(10)
    print ('heartbeat stop!')
    self.close()
  
  def __ioloop__(self):
    while self.running:
      try:
        str = self.socketChannel.readLine()
        self.client.onMessage(str)
      except Exception as e:
        print('连接发生异常 5s　后重新连接')
        time.sleep(5)
        self.client.reconnect()
        break
    print ("ioloop thread stop!")
    self.close()