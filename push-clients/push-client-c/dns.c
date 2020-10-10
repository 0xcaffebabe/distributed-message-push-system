
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <ctype.h>
#include <string.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>

char ipbuf[32];

void dns_resolve(char * host, char * result){
  extern int h_errno;
	struct hostent *h;
	struct in_addr in;
	struct sockaddr_in addr_in;
	h = gethostbyname(host);
	if(h==NULL)
	{
		printf("%s\n",hstrerror(h_errno));
    result = NULL;
	}
	else
	{
		memcpy(&addr_in.sin_addr.s_addr,h->h_addr,4);
		in.s_addr=addr_in.sin_addr.s_addr;
		sprintf(result, "%s", inet_ntoa(in));//将一个IP转换成一个互联网标准点分格式的字符串
	}
}
