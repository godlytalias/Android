#include<cstdlib>
#include<iostream>
#include<fstream>
using namespace std;
int x,a,b,i,j,c,r,q,rem,hit=0,miss=0,temp,m,tim;
float ratio;
int main()
{ int flag;
srand(time(NULL));

 
cout<<"\nEnter The cache size \n\t?=";
cin>>c;
cout<<"\nEnter block size\n\t?=";
cin>>b;
cout<<"\nEnter the associativity\n\t?=";
cin>>a;

r=c/(a*b);

int cache[r][a];
int used[r][a];

ifstream input;

int ch;

for(i=0;i<r;i++)
{
 for(j=0;j<a;j++)
 {
  cache[i][j]=-1;
  used[r][a]=-1;
  }
 }
int pointer[r];
for(i=0;i<r;i++)
pointer[i]=0;

input.open("input.txt",ios::in);
input>>x;
cout<<"\n\nWHICH ALGORITH YOU WANT TO USE\n\t1.FIFO\n\t2.LRU\n\t3.RANDOM REPLACEMENT\n\t4.EXIT\n\t?=";

cin>>ch;
switch(ch)
{
case 1:while(1)

{    if(input.eof()) break;
    input>>x;
    if(x== -1) break;
   q=(x/b)/r;
  rem=(x/b)%r;

   flag=0;
 for(i=0;i<a;i++)
{if(cache[rem][i]==q)
   {hit++;
    flag=1;
     break;
    }

}
cout<<"R: "<<rem<<"\tQ: "<<q<<endl;

 if(flag==0)
  {
    miss++;
cache[rem][pointer[rem]]=q;
   pointer[rem]++;
   if(pointer[rem]==a)
    pointer[rem]=0;
 }

}
break;
case 2:while(!input.eof())
{input>>x;
  q=(x/b)/r;
  rem=(x/b)%r;

   flag=0;
 for(i=0;i<a;i++)
{if(cache[rem][i]==q)
   {hit++;
    flag=1;
     break;
    }

}

 if(flag==0)
  {miss++;
  m=used[rem][0];i=0;
   for(j=0;j<a;j++)
   { if(used[rem][j]<m)
     {m=used[rem][j];
       i++;}
    }

cache[rem][i]=q;
tim++;
used[rem][i]=tim;
}
}
break;
case 3:
while(!input.eof())
{input>>x;

  q=(x/b)/r;
  rem=(x/b)%r;

   flag=0;
 for(i=0;i<a;i++)
{if(cache[rem][i]==q)
   {hit++;
    flag=1;
     break;
    }

}

 if(flag==0)
  {miss++;
 
   temp=rand();
   cache[rem][temp%a]=q;  
 }

}
break;
default:cout<<"INVALID ENTRY";
        break;

    }
    ratio=hit/(hit+miss);
cout<<"\nHit = "<<hit<<"\nMiss = "<<miss<<"\nHit Ratio = "<<(float)hit/(hit+miss);
input.close();

return 0;
  }
