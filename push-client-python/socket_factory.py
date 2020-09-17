
import socket

class SocketFactory:
  @classmethod
  def newSocket(cls, host, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host, port))
    return s
