const SocketChannel = require('./socket_channel').SocketChannel

class BioThreadIoManager{
  constructor(socket, client){
    this.client = client
    this.socketChannel = new SocketChannel(socket, client)
    this.running = false
  }

  send(msg){
    this.socketChannel.writeAndFlush(msg)
  }

  close(){
    this.running = false
    this.socketChannel.close()
  }

  startThread(){
    this.running = true
    this.heartbeatTimer = setInterval(() => {
      if (!this.running) {
        console.log('hearbeat thread stop!')
        clearInterval(this.heartbeatTimer)
        return;
      }
      this.heartbeat()
    }, 3000)
  }

  heartbeat(){
    try {
      this.socketChannel.writeAndFlush('heartbeat-' + this.client.userId)
    }catch(e) {
      console.error('发送心跳失败', e)
    }
  }

}

module.exports = { BioThreadIoManager }