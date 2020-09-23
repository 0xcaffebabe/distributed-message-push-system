const net = require('net')

class SocketChannel {

}

const client = net.connect(
  {port: 80, host: 'baidu.com'},
  () => {
    console.log('client conencted')
    client.write('GET / HTTP/1.1\r\nHost: www.baidu.com\r\n\r\n')
  }
)

client.on('data', data => {
  console.log(data.toString())
})
