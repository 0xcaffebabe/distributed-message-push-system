package client

import (
	"fmt"
	"io/ioutil"
	"net/http"
)

func Get(url string) (string,error){
	resp, err := http.Get(url)
	if err != nil {
		fmt.Println(err)
		return "",err
	}
	body, err := ioutil.ReadAll(resp.Body)
	return string(body), nil
}
