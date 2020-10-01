package client

import "fmt"

type BioClient struct {
	Client
	userId string
	connector *Connector
	manager *BioThreadIoManager
}

func NewBioClient(userId string) *BioClient {
	client := new(BioClient)
	client.userId = userId
	return client
}

func (client *BioClient) Connect(connector *Connector)  {
	if !connector.IsAvailable() {
		connector.LookupConnector()
	}
	client.connector = connector
	socket, err := NewSocket(connector.Host, connector.Port)
	if err != nil {
		fmt.Println("创建 socket 发生错误 ", err)
		return
	}
	client.manager = NewBioThreadIoManager(socket, client)
	client.manager.StartThread()
}

func (client *BioClient) Reconnect()  {
	if client.connector == nil {
		fmt.Println("client reconnect connector is null")
		return
	}
	client.connector.LookupConnector()
	client.Connect(client.connector)
}

func (client *BioClient) Send(msg string)  {
	if client.manager == nil {
		fmt.Println("client send manager is null")
		return
	}
	client.manager.Send(msg)
}

func (client *BioClient) Close()  {
	if client.manager != nil {
		client.manager.Close()
	}
}
