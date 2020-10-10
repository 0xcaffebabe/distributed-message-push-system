#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <unistd.h>
#include <errno.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "./socket.c"

const int MAX_LINE = 2048;
const int PORT = 1999;
const int BACKLOG = 10;
const int LISTENQ = 6666;
const int MAX_CONNECT = 20;

int main(int argc , char ** argv)
{
	WrappedSocket ws = newSocket("www.baidu.com", 80);
	
	connectServer(ws);

  writeAndFlush(ws, "GET / HTTP/1.1\r\nHost: www.baidu.com\r\nConnection: close\r\n\r\n");
  char * buf;
	while(1)	
	{
		buf = readLine(ws);
    printf("receive:%s", buf);
	}

	closeSocket(ws);
}