const net = require('net')
const socketFactory = require('./socket_factory')

class SocketChannel {

  constructor(socket,callback){
    this.socket = socket
    this.dataQueue = []
    this.callback = callback

    this.socket.on('data',data => {
      const datas = data.toString().split('\n')
      for(let i = 0;i<datas.length;i++){
        if (datas[i]) {
          this.callback(datas[i])
        }
      }
    })
  }

  writeAndFlush(msg){
    this.socket.write(msg)
  }

  close(){
    
  }
}

async function main(){
  const socket = await socketFactory.newSocket('baidu.com', 80)

  const sc = new SocketChannel(socket, data => console.log(data))
  sc.writeAndFlush('GET / HTTP/1.1\r\nHost: www.baidu.com\r\n\r\n')  
}

main()