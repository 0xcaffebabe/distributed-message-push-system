
const socketFactory = require('./socket_factory')
const BioClient = require('./bio_client').BioClient
const AutoConfrimMessageHandler = require('./auto_confirm_message_handler').AutoConfrimMessageHandler
const Connector = require('./connector').Connector

function handle(msg){
  console.log(msg)
}

const connector = new Connector('http://192.168.1.100:30001')
const client = new BioClient('9527', socketFactory)

client.messageHandler = new AutoConfrimMessageHandler(handle, client)
client.connect(connector)

console.log('client up')