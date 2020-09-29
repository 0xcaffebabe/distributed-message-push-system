package client

type MessageHandler interface {
	handle(msg string)
}

