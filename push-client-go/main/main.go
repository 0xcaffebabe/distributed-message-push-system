package main

import (
	. "../client"
	"fmt"
)
func main(){
	connector := NewConnector("http://192.168.1.12:30001")
	client := NewBioClient("9527")
	client.MessageHandler = NewAutoHandler(client)

	client.Connect(connector)

	fmt.Println("client up")

}