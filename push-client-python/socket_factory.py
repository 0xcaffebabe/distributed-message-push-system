
import socket
class SocketFactory:
  def newSocket(self, host, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host, port))
    return s


factory = SocketFactory()
s = factory.newSocket('www.baidu.com',80)
s.send('GET / HTTP/1.1\r\nHost: www.sina.com.cn\r\nConnection: close\r\n\r\n')
buffer = []
while True:
  d = s.recv(1024)
  if d:
    buffer.append(d)
  else:
    break

print (''.join(buffer))