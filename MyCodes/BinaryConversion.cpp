#include<iostream>
#include<math.h>
using namespace std;

bool paliendrome(long long int a)
{
     long long dup,rev=0;
     dup=a;
     while(dup>0)
     {
              rev=(rev*10)+(dup%10);
              dup/=10;}
      if(rev==a)
      return true;
      else
      return false;
      }
     
bool binary(long long int num)
{
     long long i=1;
     long digit=1,bin[100000],d;
     while(num>0)
     {
                 bin[0]=num%2;
                 num/=2;
                 digit++;
          
              
    d=digit;
    while(d>0)
    {
              bin[d]=bin[d-1];
              d--;} }
           
          
         d=1;
         digit-=1;
        
         cout<<"BINARY FORM =";
        
         for(long long k=1;k<=digit;k++)
         {
                  cout<<bin[k];}
                 
                  cout<<"\n";
         while(d<digit)
         {
                       if(bin[d]!=bin[digit])
                       return false;
                       d++;
                       digit--;
                       }
                       return true;      
                        
                
                }
     
 int main()
 {
     long long i;
    
     cout<<"ENTER THE NUMBER\n\n\t?=";
     cin>>i;
    
     if(binary(i))
     cout<<i<<" IS PALIENDROME IN BASE-2";
     else
     cout<<i<<" IS NOT A PALIENDROME IN BASE-2";
    
    
            
     cin>>i;
    
     return 0;
     }  
