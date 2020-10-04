
import socket

class SocketChannel:
  def __init__(self, s: socket):
    self.socket = s
    self.buffer = self.socket.makefile(mode='r')
  
  def writeAndFlush(self, msg):
    self.socket.send(bytes(msg,encoding='utf8'))
  
  def readLine(self):
    return self.buffer.readline().replace('\n', '')
  
  def close(self):
    self.socket.close()
