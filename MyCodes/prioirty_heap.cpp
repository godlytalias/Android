    #include<iostream>
    using namespace std;
    const int MAX=100;
  
    struct data
          {
           int val,p;  /* val stores the value and p stores the respective priority*/
          }d[MAX];
    int heapsize=0,length=0;
    data temp;
  
  
int parent(int i)
{return (i/2);}


int left(int i)
{return 2*i;}


int right(int i)
{return (2*i+1);}



int minheapify(data a[],int i)  /* min heapify(min value at first) using the key of the data */
{
int l=left(i);
int smallest;
int r=right(i);
if (l<=heapsize && a[l].p<a[i].p)
smallest=l;
else smallest=i;
if (r<=heapsize && a[r].p<a[smallest].p)
smallest=r;
if (!(smallest==i))
{temp=a[i];
a[i]=a[smallest];
a[smallest]=temp;
minheapify(a,smallest); }
}



int heap_min(data a[])
{
return a[1].p;
}



int buildminheap(data a[],int length)
{
heapsize=length;
for(int l=(length/2);l>1;l--)
minheapify(a,l);
}



int heapincp(data a[],int i,int ps)
{
if (ps>a[i].p)
cout<<"ERROR";
a[i].p=ps;
while((i>1) && (a[parent(i)].p>a[i].p))
{
temp=a[i];
a[i]=a[parent(i)];
a[parent(i)]=temp;
i=parent(i);
}
}



int heap_extract_min(data a[])
{
if (heapsize<1)
cout<<"ERROR UNDERFLOW";
int minimum = a[1].val;
a[1]= a[heapsize];
heapsize--;
minheapify(a,1);
return minimum;
}



int min_heap_insert(data a[],int p)
{
heapsize+=1;
a[heapsize].p = 100000000;
heapincp(a,heapsize,p);
}




int main()
    {
       char op;
      do
      {
        int ch;

  
        cout<<"----------Menu-------------\n\n";
        cout<<"\t1.Insertion\n\t2.Extract";
        cout<<"\n\n\tEnter your Choice<1,2> ?";
        cin>>ch;
        switch(ch)
        {
           case 1 :  length++;
           cout<<"Enter Value ?";
                     cin>>d[length].val;
                     cout<<"Enter Priority (KEY) ?";
                     cin>>d[length].p;
                    
                     min_heap_insert(d,d[length].p);
                     break;
         
           case 2 :   cout<<"\nPRIORITY QUEUE\n";
                 cout<< heap_extract_min(d)<<"\t";
                  length--;
                 break;
          }
          cout<<"\n\nDo You Want to Continue <Y/N> ?";
          cin>>op;
        }while(op=='Y' || op=='y');
      
        return 0;
      }
  
