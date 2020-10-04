package client

type MessageHandler interface {
	Handle(msg string)
}

