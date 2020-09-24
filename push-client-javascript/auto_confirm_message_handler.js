
class AutoConfrimMessageHandler{
  constructor(callback, client){
    this.callback = callback
    this.client = client
  }

  handle(msg){
    try {
      const maps = JSON.parse(msg)
      self.callback(maps)
      // TODO auto confirm
    } catch(e){
      console.log('接收到非结构化消息:', msg)
    }
  }
}