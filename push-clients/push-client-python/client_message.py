
class ClientMessage:
  def __init__(self):
    self.messageId = ''
    self.messageType = ''
    self.payload = ''
  def __str__(self):
    return 'id:' + self.messageId + ', type:' + self.messageType + ', payload:' + self.payload