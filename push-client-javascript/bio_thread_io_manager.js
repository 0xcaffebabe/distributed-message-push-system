const SocketChannel = require('./socket_channel').SocketChannel

class BioThreadIoManager{
  constructor(socket, client, callback){
    this.client = client
    this.socketChannel = new SocketChannel(socket, this.onMessage)
    this.running = false
    this.callback = callback
  }

  onMessage(msg){
    this.callback(msg)
  }

  send(msg){
    self.socketChannel.writeAndFlush(msg)
  }

  close(){
    this.running = false
    this.socketChannel.close()
  }

  startThread(){
    
  }

  heartbeat(){
    try {
      this.socketChannel.send('heartbeat-' + this.client.userId)
    }catch(e) {
      console.log('发送心跳失败')
    }
  }

}