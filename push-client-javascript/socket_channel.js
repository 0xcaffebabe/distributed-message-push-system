const net = require('net')

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
}

const client = net.connect(
  {port: 80, host: 'baidu.com'},
  (e) => {
    console.log(e)
  }
)

const sc = new SocketChannel(client, data => console.log(data))
sc.writeAndFlush('GET / HTTP/1.1\r\nHost: www.baidu.com\r\n\r\n')
