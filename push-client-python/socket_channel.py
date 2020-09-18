
import socket

class SocketChannel:
  def __init__(self, s: socket):
    self.socket = s
  
  def writeAndFlush(self, msg):
    self.socket.send(bytes(msg,encoding='utf8'))
  
  def readLine(self):
    line = ""
    while not line.endswith('\n'):
      line += self.socket.recv(1)
    return line
  
  def close(self):
    self.socket.close()

