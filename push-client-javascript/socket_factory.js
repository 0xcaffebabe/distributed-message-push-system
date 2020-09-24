
const net = require('net')

async function newSocket(host, ip){
  return new Promise((resolve, rejects) => {
    const client = net.connect(
      {port: 80, host: 'baidu.com'},
      (e) => {
        resolve(client)
      }
    )
  })
}

module.exports = { newSocket }