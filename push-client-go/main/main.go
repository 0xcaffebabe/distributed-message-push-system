package main

import (
	. "../client"
	"fmt"
	"io"
)
func main(){
	socket, err := NewSocket("192.168.1.101", 1999)
	if err != nil {
		fmt.Println("建立 socket 发生错误:", err)
		return
	}
	socketChannel := NewSocketChannel(socket)
	//socketChannel.WriteAndFlush("GET / HTTP/1.0\r\n\r\n")
	for {
		line ,err := socketChannel.ReadLine()
		if err == io.EOF {
			fmt.Println("连接已关闭")
			break
		}
		fmt.Println(line)
	}

}