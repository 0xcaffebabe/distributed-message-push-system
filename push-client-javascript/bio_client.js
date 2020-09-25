const BioThreadIoManager  = require('./bio_thread_io_manager').BioThreadIoManager
const Client = require('./client').Client
class BioClient extends Client{
  constructor(userId, socketFactory){
    this.userId = userId
    this.socketFactory = socketFactory
  }

  connect(connector) {
    if (!connector.isAvailable()) {
      connector.lookupConnector()
    }

    this.connector = connector
    const socket = this.socketFactory.newSocket(connector.host, connector.port)
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