package client

import (
	"bufio"
	"net"
	"strconv"
)

type Socket struct {
	conn net.Conn
	reader *bufio.Reader
}

func NewSocket(host string, port int) (*Socket, error) {
	conn, err := net.Dial("tcp", host + ":" +strconv.Itoa(port))
	if err!= nil {
		return nil, err
	}
	socket := new(Socket)
	socket.conn = conn
	socket.reader = bufio.NewReader(conn)
	return socket, nil
}

func (socket *Socket) ReadLine() ([]byte, error){
	line, _, err := socket.reader.ReadLine()
	if err != nil {
		return nil, err
	}
	return line, nil
}

func (socket *Socket) Write(data []byte) error{
	_, err := socket.conn.Write(data)
	return err
}

func (socket *Socket) Close()  {
	socket.conn.Close()
}