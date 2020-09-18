from socket_factory import *
from socket_channel import *
print ('hello world')

s = SocketFactory.newSocket('192.168.1.101', 1999)
channel = SocketChannel(s)
channel.writeAndFlush('i am you father')
channel.readLine()