const BioThreadIoManager  = require('./bio_thread_io_manager').BioThreadIoManager
const Client = require('./client').Client
class BioClient extends Client{
  constructor(userId, socketFactory){
    super()
    this.userId = userId
    this.socketFactory = socketFactory
  }

  async connect(connector) {
    if (!connector.isAvailable()) {
      await connector.lookupConnector()
    }

    this.connector = connector
    const socket = await this.socketFactory.newSocket(connector.host, connector.port)
    this.manager = new BioThreadIoManager(socket, this, this.onMessage)
    this.manager.startThread()
  }

  reconnect(){
    if (!this.connector) {
      throw new Error('connector is null!')
    }

    this.connector.lookupConnector()
    this.connect(this.connector)
  }

  send(msg) {
    if (!this.manager) {
      throw new Error('manager is null')
    }
    this.manager.send(msg)
  }

  close() {
    if (this.manager) {
      this.manager.close()
    }
  }
}

module.exports = { BioClient }