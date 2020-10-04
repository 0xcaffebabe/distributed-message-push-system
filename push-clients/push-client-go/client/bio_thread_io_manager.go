package client

import (
	"fmt"
	"sync"
	"time"
)

type BioThreadIoManager struct {
	client *BioClient
	sc *SocketChannel
	running bool
	wg sync.WaitGroup
}

func NewBioThreadIoManager(socket *Socket, client *BioClient) *BioThreadIoManager{
	manager := new(BioThreadIoManager)
	manager.sc = NewSocketChannel(socket)
	manager.client = client
	manager.running = false
	return manager
}

func (manager *BioThreadIoManager) Send(msg string)  {
	err := manager.sc.WriteAndFlush(msg)
	if err != nil {
		fmt.Println("manager.WriteAndFlush 发生错误 ", err)
	}
}

func (manager *BioThreadIoManager) Close()  {
	manager.running = false
	manager.sc.Close()
}

func (manager *BioThreadIoManager) StartThread()  {
	manager.running = true
	manager.wg.Add(2)
	go manager.heartbeat()
	go manager.ioloop()
	manager.wg.Wait()
}

func (manager *BioThreadIoManager) heartbeat()  {
	for manager.running {
		manager.Send("heartbeat-" + manager.client.userId)
		time.Sleep(time.Second * 10)
	}
	fmt.Println("heartbeat stop!")
	manager.Close()
	manager.wg.Done()
}

func (manager *BioThreadIoManager) ioloop()  {
	for manager.running {
		msg, err := manager.sc.ReadLine()
		if err != nil {
			fmt.Println("manager.sc.ReadLine 发生异常 ", err)
			fmt.Println("5s 后重新连接")
			time.Sleep(time.Second * 5)
			manager.client.Reconnect()
			break
		}
		manager.client.OnMessage(msg)
	}
	fmt.Println("ioloop stop!")
	manager.Close()
	manager.wg.Done()
}