#include<iostream>
using namespace std;
long a[1000];
void merge(long a[],int p,int q,int r)
{
    int i,j,k;
    long l[1000],m[1000];
    int n1 = q-p+1;
    int n2 = r-q;
    for(i=1;i<=n1;i++)
        l[i]=a[p+i-1];
    for(j=1;j<=n2;j++)
        m[j]=a[q+j];
    m[j]=l[i]=9999999;
    i=1;
    j=1;
    for(k=p;k<=r;k++)
    {
        if (l[i]<m[j])
        {
            a[k]=l[i];
            i++;
        }
        else
        {
            a[k]=m[j];
            j++;
        }
    }
}

void mergesort(long a[],int p,int r)
{
    if(p<r)
    {
        int q=(p+r)/2;
        mergesort(a,p,q);
        mergesort(a,(q+1),r);
        merge(a,p,q,r);
    }
}


int main()
{
    cout<<"ENTER THE NO: OF INPUTS : ";
    int no;
    cin>>no;
  
    for(int c=1;c<=no;c++){
        cout<<"\nENTER INPUT NO. "<<c<<"  :   ";
        cin>>a[c];}
        mergesort(a,1,no);
        cout<<"\n* SORTED ARRAY *\n";
        for(int i=1;i<=no;i++)
            cout<<a[i]<<"  ";
        cout<<"\n";
        return 0;
}
