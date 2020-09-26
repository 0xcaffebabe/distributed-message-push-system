
const net = require('net')

async function newSocket(host, port){
  return new Promise((resolve, rejects) => {
    const client = net.connect(
      {host, port},
      (e) => {
        resolve(client)
      }
    )
  })
}

module.exports = { newSocket }