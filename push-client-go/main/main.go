package main
import . "../client"
func main(){
	conn := NewConnector("http://192.168.1.12:30001")
	conn.LookupConnector()
	println(conn.Port)
	println(conn.Host)
	println(conn.IsAvailable())
}