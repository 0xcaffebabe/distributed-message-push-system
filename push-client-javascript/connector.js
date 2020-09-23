let axios = require('axios')

class Connector {
  constructor(lookupAddress) {
    this.lookupAddress = lookupAddress
    this.host = ''
    this.port = 0
  }

  async lookupConnector() {
      const data = await axios.get(this.lookupAddress)
      const splitResult = data.data.split(':')
      if (splitResult.length !== 2) {
        throw new Error('获取 connector 失败')
      }
      this.host = splitResult[0]
      this.port = parseInt(splitResult[1])
  }

  isAvailable() {
    return this.host && this.port !== 0
  }
}

let conenctor = new Connector('http://localhost:30001')
conenctor.lookupConnector()
console.log(conenctor.isAvailable())