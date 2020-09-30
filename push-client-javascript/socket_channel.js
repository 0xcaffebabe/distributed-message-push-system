const net = require('net')
const socketFactory = require('./socket_factory')

class SocketChannel {

  constructor(socket,client){
    this.socket = socket
    this.dataQueue = []
    this.client = client

    this.socket.on('data',data => {
      const datas = data.toString().split('\n')
      for(let i = 0;i<datas.length;i++){
        if (datas[i]) {
          this.client.onMessage(datas[i])
        }
      }
    })
  }

  writeAndFlush(msg){
    this.socket.write(msg)
  }

  close(){
    this.socket.destroy()
  }
}

module.exports = { SocketChannel }