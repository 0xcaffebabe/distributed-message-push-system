#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>

typedef struct{
  char* host;
  int port;
  int sockfd;
	struct sockaddr_in servaddr;
} WrappedSocket;

ssize_t readline(int fd, char *vptr, size_t maxlen)
{
	ssize_t	n, rc;
	char	c, *ptr;

	ptr = vptr;
	for (n = 1; n < maxlen; n++) {
		if ( (rc = read(fd, &c,1)) == 1) {
			*ptr++ = c;
			if (c == '\n')
				break;	/* newline is stored, like fgets() */
		} else if (rc == 0) {
			*ptr = 0;
			return(n - 1);	/* EOF, n - 1 bytes were read */
		} else
			return(-1);		/* error, errno set by read() */
	}

	*ptr = 0;	/* null terminate like fgets() */
	return(n);
}

WrappedSocket newSocket(char* host, int port){
  WrappedSocket ws;
  ws.sockfd = socket(AF_INET , SOCK_STREAM , 0);
  if (ws.sockfd == -1) {
    perror("socket error");
    exit(1);
  }
  bzero(&ws.servaddr , sizeof(ws.servaddr));
	ws.servaddr.sin_family = AF_INET;
	ws.servaddr.sin_port = htons(port);
  if(inet_pton(AF_INET , host , &ws.servaddr.sin_addr) < 0)
	{
		printf("inet_pton error for %s\n", host);
		exit(1);
	}
  return ws;
}

void connectServer(WrappedSocket ws){
  /*发送链接服务器请求*/
  if( connect(ws.sockfd , (struct sockaddr *)&ws.servaddr , sizeof(ws.servaddr)) < 0)
	{
		perror("connect error");
		exit(1);
	}
}
char buf[1024];
char* readLine(WrappedSocket ws){
  if(readline(ws.sockfd , buf , 1024) == 0)
	{
		perror("server terminated prematurely");
		exit(1);
	}
  return buf;
}

void writeAndFlush(WrappedSocket ws, char * msg){
  if (write(ws.sockfd , msg , strlen(msg)) == 0){
    printf("write zero");
  }
}

void closeSocket(WrappedSocket ws){
  close(ws.sockfd);
}