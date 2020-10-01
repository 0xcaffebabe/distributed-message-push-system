package client

type ClientMessage struct {
	MessageId string `json:"messageId"`
	MessageType string `json:"messageType"`
	Payload string `json:"payload"`
}
