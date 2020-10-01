package client

type Client struct {
	MessageHandler MessageHandler
}

func (client *Client) OnMessage(msg string)  {
	if client.MessageHandler != nil {
		client.MessageHandler.Handle(msg)
	}
}