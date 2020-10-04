package client

import (
	"fmt"
	"strconv"
	"strings"
)

type Connector struct {
	lookupAddress string
	Host string
	Port int
}
func NewConnector(lookupAddress string) *Connector{
	conn := new(Connector)
	conn.lookupAddress = lookupAddress
	return conn
}
func (conn *Connector) LookupConnector()  {
	resp, err := Get(conn.lookupAddress)
	if err != nil {
		fmt.Println("获取 connector 发生异常", err)
	}
	splitResult := strings.Split(resp, ":")

	if len(splitResult) != 2 {
		fmt.Println("获取 connector 失败", resp)
		return
	}

	conn.Host = splitResult[0]
	conn.Port, _ = strconv.Atoi(splitResult[1])
}
func (conn *Connector) IsAvailable() bool {
	return conn.Host != "" && conn.Port > 0
}
