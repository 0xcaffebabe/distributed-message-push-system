
class AutoConfrimMessageHandler{
  constructor(callback, client){
    this.callback = callback
    this.client = client
  }

  handle(msg){
    try {
      const maps = JSON.parse(msg)
      this.callback(maps)
      this.client.send('confirm-' + maps.messageId)
    } catch(e){
      console.log('接收到非结构化消息:', msg)
    }
  }
}

module.exports = { AutoConfrimMessageHandler }