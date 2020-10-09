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
	WrappedSocket ws = newSocket("127.0.0.1", 1999);
	
	connectServer(ws);

  writeAndFlush(ws, "hello world");
  char * buf;
	while(1)	
	{
		buf = readLine(ws);
    printf("receive:%s", buf);
	}

	closeSocket(ws);
}