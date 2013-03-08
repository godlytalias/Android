/*compile the code for SERVER and CLIENT as two separate programs with the argument -lpthread 

example :   gcc -o server server.c -lpthread
                   gcc -o client client.c -lpthread

and then execute in the order
   ./server <port no>
   ./client <server ip> <port no>
*/

 

SERVER

#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<errno.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<pthread.h>

FILE *fp;

struct userinfo
{
char username[100];
int socket;
}u[1000];

pthread_t thread;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

char buffer[256],user[100],str[100],pass[30];
 struct sockaddr_in serv_addr, cli_addr;
 int n,nu=1,i;

void error(char *msg) 
 {
 perror(msg); 
 exit(0);
 }




void* server(void*);


int main (int argc, char *argv[]) 
 {

fp=fopen("user.txt","w");
fprintf(fp,"server started\n");
fclose(fp);

  int i,sockfd, newsockfd[1000], portno, clilen,no=0,n;
 

  
 if (argc<2) 
  {
  fprintf (stderr,"ERROR! Provide A Port!\n");
  exit(1);
  }
  
 sockfd = socket(AF_INET, SOCK_STREAM, 0);
  
 if (sockfd<0) 
  error("ERROR! Cannnot Open Socket");
  
 bzero((char *) &serv_addr, sizeof(serv_addr));
  
 portno = atoi(argv[1]);
 serv_addr.sin_family = AF_INET;
 serv_addr.sin_addr.s_addr = INADDR_ANY;
 serv_addr.sin_port = htons(portno);
  
 if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr))<0)
  error("ERROR! On Binding");
  
 listen(sockfd, 5);

  clilen = sizeof(cli_addr);



while(1)
{
 newsockfd[no] = accept(sockfd, (struct sockaddr *)&cli_addr, &clilen);
 
 if (newsockfd<0)
  error("ERROR! On Accepting");

  
 
if(n<0)
error("ERROR READING FROM SOCKET");


pthread_mutex_lock(&mutex);
pthread_create(&thread,NULL,server,(void*)&newsockfd[no]);
no+=1;

}
  
 
 close(newsockfd[no]); 
 
 close(sockfd);
 return 0;
  
 }

void* server(void* sock)
{
int newsockfd=*((int*)sock),j=0,m;


char to[100],from[100],name[100];
fp=fopen("user.txt","r+");

checking:
m=1;
bzero(user,100);
bzero(to,100);
bzero(from,100);
bzero(str,100);
recv(newsockfd, user,100,0);
recv(newsockfd, pass,30,0);
while(fscanf(fp,"%s",str)!=EOF)
{

n=strncmp(user,str,strlen(str));
if(n==0)
{
fscanf(fp,"%s",str);
n=strncmp(pass,str,strlen(str));
if(n==0)
{
m=2;
break;
}
else
{
send(newsockfd,"USERNAME EXISTS",15,0);
m=0;
break;}
fscanf(fp,"%s",str);
}

}
if(m==0)
goto checking;
if(m==1)
{
fclose(fp);
send(newsockfd,"USER REGISTERED",15,0);
bzero(u[nu].username,100);
u[nu].socket=newsockfd;
strncpy(u[nu].username,user,strlen(user));
nu++;
}
if(m==2)
{
fclose(fp);
send(newsockfd,"USER LOGGED IN",14,0);
for(i=1;i<nu;i++)
if(strcmp(user,u[i].username)==0)
break;
u[i].socket=newsockfd;
}
pthread_mutex_unlock(&mutex);

bzero(buffer, 256);
int newsockfd1;
while(1)
{ 
 n = recv(newsockfd, buffer, 255, 0);
if(n<0)
  error("ERROR! Reading From Socket");
  
  if(strncmp(buffer,"bye",3)==0)
  {
  close(newsockfd);
  pthread_exit(NULL);
  } 
  
 
i=3;

strcpy(name,buffer);
while(name[i]!=':')
{
to[i-3]=name[i];
i++;
}
to[i-3]='\0';
j=0;
bzero(buffer,256);
while(name[i]!='|')
{
buffer[j]=name[i];
i++;
j++;
}
buffer[j]='\0';
j=0;
for(i+=1;name[i]!='\0';i++)
{
from[j]=name[i];
j++;
}
from[j-1]='\0';

printf("To %s  From %s Message %s",to,from,buffer);
 
for(j=1;j<nu;j++)
{
if((strncmp(u[j].username,to,strlen(to)))==0)
{
newsockfd1=u[j].socket;
break;
}
}
strcat(from,buffer);
bzero(buffer,256);
strcpy(buffer,"From ");
strcat(buffer,from); 
 
 n=send(newsockfd1,buffer,sizeof buffer,0);
if(n<0)
{
send(newsockfd, "SENDING FAILED : USER LOGGED OUT",32,0); 
}
else
{
n = send(newsockfd, "Message sent", 18, 0);
  
 if (n<0)
  error("ERROR! Writing To Socket");}
}
close(newsockfd);
pthread_exit(NULL);
}



CLIENT 


#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<sys/types.h>
#include<errno.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<netdb.h>
#include<pthread.h>


pthread_t thread;



void error (char *msg){
 perror(msg);
 exit(0);
 }


void* receive(void* d)
{
int sockfd=*((int *)d);
int a;
char buf[256];

while(1)
{
bzero(buf,256);
a=recv(sockfd,buf,255,0);
if(a<=0)
error("Error reading from socket");
else
printf("\n %s\n ",buf);
}
close(sockfd);
pthread_exit(NULL);
}

  
 int main (int argc, char *argv[]) {
  int sockfd, portno, n,newsockfd,userno;
char user[100],pass[30];
  struct sockaddr_in addr_in, serv_addr;
  struct hostent *server;
  char buffer[256];
  FILE *file;
  

   if (argc<3) {
   fprintf (stderr,"Usage %s Host Name Port\n", argv[0]);
   exit(0);
   }
   
   portno=atoi(argv[2]);
   sockfd=socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);


    if (sockfd<0)
   error("ERROR Opening Socket");
   
   server=gethostbyname(argv[1]);
   
   if (server==NULL) {
    fprintf(stderr, "ERROR, No Such Host\n");
    exit(0);
    }
    
    bzero((char*)&serv_addr, sizeof(serv_addr));
    
    serv_addr.sin_family=AF_INET;
    bcopy((char*)server->h_addr, (char*)&serv_addr.sin_addr.s_addr, server->h_length);
    serv_addr.sin_port=htons(portno);
    newsockfd = connect(sockfd, (struct sockaddr *)&serv_addr, sizeof(struct sockaddr));
    if (newsockfd<0)
    error("ERROR Connecting");
    

else
{
reg:
bzero(user,100);
username:
printf("\nENTER THE USERNAME  : ");
fgets(user,100,stdin);
if(strncmp(user,"user-",5)!=0)
{
printf("USERNAME FORMAT NOT CORRECT  (FORMAT - user-<no>)");
goto username;
}
n=send(sockfd, user, strlen(user), 0);
printf("\nENTER THE PASSWORD  : ");
fgets(pass,30,stdin);
n=send(sockfd, pass, strlen(pass), 0);
bzero(buffer,256);
n=recv(sockfd,buffer,255,0);
if(n>0)
printf("%s\n",buffer);
if(strcmp(buffer,"USERNAME EXISTS")==0)
goto reg;
}
file=fopen("user.txt","a+");
char str[100];
bzero(str,100);
printf("\nLOGGED IN USERS\n");
while(fscanf(file,"%s",str)!=EOF)
{
if(str[0]=='u')
printf("\n%s\n",str);
fscanf(file,"%s",str);
}
if(strcmp(buffer,"USER REGISTERED")==0)
{
fprintf(file,"%s",user);
fprintf(file,"\n%s\n",pass);}
fclose(file);


pthread_create(&thread,NULL,receive,(void*)&sockfd);
while(1)
{

enter:

     printf("\nPlease Enter The Message: ");
    
    bzero(buffer, 256);
    fgets(buffer, 255, stdin);
    
if((strncmp(buffer,"To user-",8)==0)||(strncmp(buffer,"bye",3)==0))
{
sprintf(buffer,"%s|%s",buffer,user);
n=send(sockfd, buffer, strlen(buffer), 0);
    
    if(n<0)
    error("ERROR Writing To Socket");
   if(strncmp(buffer,"bye",3)==0)
  break;   
    
    
}
else
{
printf("\nMESSAGE FORMAT NOT CORRECT\n");
goto enter;}

     }
    close(sockfd); 
   
   return 0;
   
  }
   
   
