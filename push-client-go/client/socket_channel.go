package client

type SocketChannel struct {
	socket  *Socket
}

func NewSocketChannel(socket *Socket) *SocketChannel {
	sc := new(SocketChannel)
	sc.socket = socket
	return sc
}

func (sc *SocketChannel) WriteAndFlush(msg string) error {
	return sc.socket.Write([]byte(msg))
}

func (sc *SocketChannel) ReadLine() (string, error) {
	line, err := sc.socket.ReadLine()
	return string(line), err
}