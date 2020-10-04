package client

import (
	"encoding/json"
	"fmt"
)

type AutoConfirmMessageHandler struct {
	client *BioClient
}

func NewAutoHandler(client *BioClient) *AutoConfirmMessageHandler{
	handler := new(AutoConfirmMessageHandler)
	handler.client = client
	return handler
}

func (handler *AutoConfirmMessageHandler) Handle(msg string)  {
	var clientMessage ClientMessage
	err := json.Unmarshal([]byte(msg),&clientMessage)
	if err != nil {
		fmt.Println("接收到非结构化消息:" + msg)
	}else {
		fmt.Println("结构化消息:", clientMessage)
		handler.client.Send("confirm-" + clientMessage.MessageId)
	}
}