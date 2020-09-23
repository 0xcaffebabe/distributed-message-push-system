
class Client {
  constructor(messageHandler){
    this.messageHandler = messageHandler
  }

  onMessage(msg){
    if (this.messageHandler) {
      this.messageHandler.handle(msg)
    }
  }
}