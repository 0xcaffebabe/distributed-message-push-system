
from client import *
from connector import *
from socket_factory import *
from bio_thread_io_manager import *
class BioClient(Client):

  def __init__(self, userId, socketFactory: SocketFactory):
    self.userId = userId
    self.socketFactory = socketFactory
  
  def connect(self, connector: Connector):
    if not connector.isAvailable():
      connector.lookupConnector()
    
    self.connector = connector
    s = self.socketFactory.newSocket(connector.host, connector.port)
    self.manager = BioThreadIoManager(s, self)
    self.manager.startThread()
  
  def reconnect(self):
    if self.connector is None:
      raise IOError('connector is null')
    
    self.connector.lookupConnector()
    self.connect(self.connector)
  
  def send(self, msg):
    if self.manager is None :
      raise Exception('manager is null')
    
    self.manager.send(msg)
  
  def close(self):
    if self.manager is not None:
      self.manager.close()