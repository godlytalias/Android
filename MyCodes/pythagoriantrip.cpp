#include<iostream>
using namespace std;

int triangle(int p)
{
    int a=1,sol=0,b,c;
    while(a<p/3)
    {
        b=1;
        while(b<p/2)
    {
            c=2;
            while(c<p)
            {
                if ((a+b+c)==p and ((c*c)==(a*a)+(b*b)))
                    {sol+=1;
                    cout<<"\na="<<a<<"\tb="<<b<<"\tc="<<c;}
                c+=1;
            }
            b+=1;
        }
        a+=1;
    }
    return sol;}
  
    int main()
    {
        int soln=0,x;
        cout<<"\nENTER THE PERIMETER\n\n\t?=";
        cin>>x;
        cout<<"\n"<<triangle(x);
        return 0;
        }


