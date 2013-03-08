#include<iostream>
#include<string.h>
#include<math.h>
using namespace std;
int v,edge,c,b,adj[100][100],dfstime=0,x,y,i;



struct vertex
{
char colour[6];
int p;
int f,d,key,small; }a[100];

void dfs_visit(vertex *g,int u)
{
dfstime=dfstime+1;
g[u].d=dfstime;
g[u].small=dfstime;
strcpy(g[u].colour,"gray");
for(int j=1;j<=v;j++)
{
    if (adj[u][j]==1)
    {
        if(!strcmp(g[j].colour,"white"))
      
{
g[j].p=u;
dfs_visit(g,j);

if(!((g[j].small)<=g[u].d))
    cout<<"BRIDGE : "<<g[j].key<<" "<<g[u].key<<"\n";
else
    g[u].small=g[j].small;}
    else
    {
        if(g[u].p!=j)
            if(g[j].d<g[u].small)
                g[u].small=g[j].small;}}
    else
        continue;
}
strcpy(g[u].colour,"black");
dfstime+=1;
g[u].f=dfstime;
}

void dfs(vertex *a)
{
for(i=1;i<=v;i++)
{
    strcpy(a[i].colour,"white");
a[i].p=0;}
dfstime=0;
for(i=1;i<=v;i++)
{
if (!strcmp(a[i].colour,"white"))
dfs_visit(a,i);}}

int main()
{
cout<<"ENTER NO: OF VERTICES\n";
cin>>v;
cout<<"Enter no: of edges\n";
cin>>edge;
cout<<"enter edges as pair of vertices";
for(int k=1;k<=edge;k++)
{
cin>>c>>b;
adj[c][b]=1;
adj[b][c]=1;
}
cout<<"ENTER THE VERTICES\n";
for(int k=1;k<=v;k++)
cin>>a[k].key;
dfs(a);
cout<<"\nVertix\tStart Time\tFinishing Time\tColour";
for(i=1;i<=v;i++)

    cout<<"\n"<<a[i].key<<"\t"<<a[i].d<<"\t "<<a[i].f<<"\t "<<a[i].colour;
return 0;
}
