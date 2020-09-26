
class Client {
  constructor(){}

  onMessage(msg){
    if (this.messageHandler) {
      this.messageHandler.handle(msg)
    }
  }
}

module.exports = { Client }