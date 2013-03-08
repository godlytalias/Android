/*  bst.cpp */

/*TO SEE THE .EXE FILE OF THIS CODE GO TO 
 * http://athena.nitc.ac.in/~godly_bcs10/Binary_Tree_App.exe

 * Copyright (C) 2010 Godly T.Alias
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

*/

#include<iostream>
#include<iomanip>
#include<conio.h>
#include<fstream>
#include<string.h>

using namespace std;

template<class T> 

//class for the stack to push and pop the nodes                                           
class stack
{
      private:
             T element[10000];
             int head;
      public:
             void push(T ele)
             {
                  if(head<=10000)
                  {
                  element[head]=ele;
                  head++;
                  }
                  else
                  cout<<"STACK FULL";
                  }
             T pop(void)
             {
                        if(head<=0)
                        return false;
                        else{
                        head--;
                        return element[head];
                        }
                        }
             stack()
             {
                    head=0;
                    }
                 
                               };


//struct type for the nodes
struct node
{
       int val,space;
       node* left_child;
       node* right_child;
       };
      
//struct for defining the relation between parent and child
struct parchild
{
       node* parent;
       int flag; //0 for left child and 1 for right child
       };
      
node* nodar[10000];
int no=0;
     
//the main class
class node_family
{
      private:
              void child(node* rt,node* a,int tab)  //function to find child and to set a space for that node so that it will be convenient
              {                                     //while outputing it in binary tree format
                   if(a->val==rt->val)
                   no--;
               if(a->val>rt->val)
               {
                                if(rt->right_child==NULL)
                                {
                                rt->right_child=a; a->space=tab;}
                               else
                                child(rt->right_child,a,tab+5); 
                                }
               if(a->val<rt->val)
               {
                                if(rt->left_child==NULL)
                               { rt->left_child=a; a->space=tab;}
                                else
                                child(rt->left_child,a,tab+5); 
               }   }
              
                             
               void display(node* nod,int space)                //function for displaying the binary tree
               {
             
                    node* l;
                    if(nod!=NULL)
                    {
                    cout<<"\n"<<setw(nod->space)<<nod->val;
                    if(nod->left_child!=NULL)
                    {                        st.push(nod);
                    display(nod->left_child,nod->space);
                                                                                    }
                    if(nod->left_child==NULL && nod->right_child!=NULL)
                    {
                                             cout<<"\n";
                                             cout<<setw((nod->space)+5)<<"()";
                                                                              
                                           display(nod->right_child,nod->space);
                                            
                                            
                                             }
                    if(nod->left_child==NULL && nod->right_child==NULL)
                    {
                                           
                                             if(l=st.pop())
                                             {
                                                                                                    
                                         display(l->right_child,(l->space+5));  } 
                                             }
                                             }
                                             else
                                             {
                                                 cout<<"\n";
                                                 cout<<setw(space+1)<<"()\n";
                                                 if(l=st.pop())
                                                {
                                                                                 
                                              display(l->right_child,(l->space+5));}
                                                 }
                                            
                  }
                 
void printfile(node* nod,int space)  //print the output to a file 'bst.txt'
               {
             
             
                    node* l;
                    if(nod!=NULL)
                    {
                    fil<<"\n"<<setw(nod->space*2)<<nod->val;
                    if(nod->left_child!=NULL)
                    {                        st.push(nod);
                    printfile(nod->left_child,nod->space);
                                                                                    }
                    if(nod->left_child==NULL && nod->right_child!=NULL)
                    {
                                             fil<<"\n";
                                             fil<<setw(((nod->space)+5)*2)<<"()";
                                                                              
                                           printfile(nod->right_child,nod->space*2);
                                            
                                            
                                             }
                    if(nod->left_child==NULL && nod->right_child==NULL)
                    {
                                           
                                             if(l=st.pop())
                                             {
                                                                                                    
                                         printfile(l->right_child,(l->space+5)*2);  } 
                                             }
                                             }
                                             else
                                             {
                                                 fil<<"\n";
                                                 fil<<setw(space+1)<<"()\n";
                                                 if(l=st.pop())
                                                {
                                                                                 
                                              printfile(l->right_child,(l->space+5)*2);}
                                                 }
                                                
                                            
                  }

                 
          parchild parentof(node* n)         //returns a struct parchild which contains the address of the parent of given node
              {
                   pc.parent=&root;
                   if(n->val==pc.parent->val)
                   pc.parent=NULL;
                   else
                   {
                   while(true)
                   { if(n->val>pc.parent->val)
                   {if(pc.parent->right_child->val!=n->val)
                     {pc.parent=pc.parent->right_child;}
                     else {pc.flag=1; break;}}
                     else
                     {
                         if(pc.parent->left_child->val!=n->val)
                     {pc.parent=pc.parent->left_child;}
                     else {pc.flag=0; break;}}
                     }}
                    return pc;
                   }
                  
                  
              node* nullleft(node* n) //return the node with left branch NULL below the passed node
              {node* s=n;
                     if(s!=NULL)
                    while(s->left_child!=NULL)
                    s=s->left_child;
                    return s;
                    }
                   
              void spaceDec(node* n,int a)    //decrease the space of the passed node and all its children by a value passed
              {
                   if(n!=NULL)
                   {
                              n->space-=a;
                              spaceDec(n->left_child,a);
                              spaceDec(n->right_child,a);
                              }}
             
              
      public:
            
            
             parchild pc;
             node root;
             stack<node*> st;
             fstream fil;
             node_family()  //constructor
             {
                         
              fil.open("bst.txt",ios::out|ios::app);
              }
             
              ~node_family()
              {
                            fil.close();
                            }
              node* findNode(int val)  //returns the address of a node with a given value
              {
                    node* s=&root;
                    while(s->val!=val)
                    {
                                      if(val>s->val)
                                      s=s->right_child;
                                      else
                                      s=s->left_child;
                                      }
                                      return s;
                    }
                           
             void deleteval(int val,node* s)  //deletes a node with a given value
             {
               
                  node* t;
                 
                  if(s->val==val)
                  {     if(s->right_child!=NULL) t=nullleft(s->right_child);
                  else  { 
                         if(parentof(s).parent==NULL)
                         {
                                                     if(s->left_child==NULL)
                                                     {
                                                                            cout<<"\nNOT ALLOWED TO DELETE THE TREE FULLY\n\a";
                                                                            return;
                                                                            }
                                                     root=*(s->left_child);
                                                     return;
                                                     }
                         else
                         {  
                            if(parentof(s).flag==1)
                          { if(s->left_child==NULL) parentof(s).parent->right_child=NULL; else{ parentof(s).parent->right_child=s->left_child; spaceDec(s->left_child,5);}}
                            else if(parentof(s).flag==0)
                       { if(s->left_child==NULL)    parentof(s).parent->left_child=NULL; else{ parentof(s).parent->left_child=s->left_child; spaceDec(s->left_child,5);}}
                             return;
                             }}
                 
                  if(t->right_child!=NULL)
                       {if(parentof(t).flag==0)
                          parentof(t).parent->left_child=t->right_child;
                          else
                          parentof(t).parent->right_child=t->right_child;
                          spaceDec(t->right_child,5);
                            } 
                  t->left_child=s->left_child; t->space=s->space;
                 
                 if(t->right_child==NULL)
                       {if(parentof(t).flag==0) parentof(t).parent->left_child=NULL; else parentof(t).parent->right_child=NULL;}
                       else
                  if (t->val!=s->right_child->val) t->right_child=s->right_child;
                           if(s->val==root.val) root=*t;
                                      
                    if(parentof(s).parent!=NULL){   if(parentof(s).flag==0) parentof(s).parent->left_child=t; else parentof(s).parent->right_child=t;
                          }  }
                  else
                  {
                     t=findNode(val);
                      deleteval(val,t);
                      } 
                            } 
                           
             bool search(int n)  //search for a value in the tree and return boolean values
             {
                  node s=root;
                  while(true)
                  {
                         if(s.val==n)
                         return true;
                         else{
                              if((n>s.val && s.right_child==NULL)||(n<s.val && s.left_child==NULL))
                              return false;
                              else
                              {
                                  if(n>s.val)
                                  s=*s.right_child;
                                  else
                                  s=*s.left_child;
                                  }
                              }} }
                             
               void printpath(int val)  //outputs the path of a node in the binary tree with the passed value
                     {
                                  node s=root;
                                  cout<<"\n";
                                  int space=40;
                                  while(s.val!=val)
                                  {
                                             cout<<setw(space)<<s.val;
                                             if(val>s.val)
                                             {
                                                          cout<<"\n";
                                                          cout<<setw(space+2)<<"->\n";
                                                          space+=2;
                                                          s=*(s.right_child);
                                                          }
                                             else
                                             {
                                                 cout<<"\n";
                                                 cout<<setw(space)<<"<-\n";
                                                 space-=2;
                                                 s=*(s.left_child);
                                                 }
                                                 }
                                                 cout<<setw(space)<<val;
                                                }
                                                       
            
            
             void printtofile(void)
             {fil<<"=========================================================";
                 fil<<"\n\t\t\t\t[NODE]_\n";
                  fil<<"\t\t\t\t               |\n";
                  fil<<"\t\t\t\t               -[LEFT CHILD]\n";
                  fil<<"\t\t\t\t               -[RIGHT CHILD]\n\n";
                 fil<<"\t\t**************\n\t\t*BINARY TREE*\n\t\t**************\n\n";

                if(root.left_child!=NULL || root.right_child!=NULL)
                                         printfile(&root,0);
                  else
                  fil<<"\n\t\t"<<root.val;
                fil<<"\n=========================================================\n";
                fil.close();
                 return;
                
                 }
                        
             node* noding(node& a,int num)  //function for initializing the nodes
             {
            
                  a.val=num;
                  a.space=0;
                  a.left_child=NULL;
                  a.right_child=NULL;
                  return &a;
                  }
                 
             void disp_tree(node roots)
             {
                 cout<<"\n\n____________________________________________________________________\n";
                  cout<<setw(20)<<"BINARY TREE\n";
                  cout<<setw(20)<<"___________\n";
                  cout<<"\t\t\t\t\t[NODE]_\n";
                  cout<<"\t\t\t\t\t       |\n";
                  cout<<"\t\t\t\t\t       -[LEFT CHILD]\n";
                  cout<<"\t\t\t\t\t       -[RIGHT CHILD]\n\n";
                  if(roots.left_child!=NULL || roots.right_child!=NULL)
                                         display(&roots,0);
                  else
                  cout<<"\n\t\t"<<roots.val;
                 
             cout<<"\n\n____________________________________________________________________\n";
             }
            
             void connect_node(node* nod) //function for connecting the nodes in the binary tree format
             {
                child(&root,nod,5);}
            
             };
            
int main()
{
     cout<<setw(30)<<"BINARY SEARCH TREE  Copyright (C) 2010 Godly T.Alias";
     node_family ob;

node elements[10000];
 int choice;
 char file;
   do
   {
                          cout<<"\n\n\t\t    ++++++++++++\n\t\t    +   MENU   +\n\t\t    ++++++++++++";
                          cout<<"\n\n\t\t __________________";
                          cout<<"\n  \t\t|   1.INSERT       |\n";
                          cout<<"  \n\t\t|   2.DELETE       |\n";
                          cout<<"  \n\t\t|   3.DISPLAY      |\n";
                          cout<<"  \n\t\t|   4.SEARCH       |\n";
                          cout<<"  \n\t\t|   5.EXIT         |\n";
                          cout<<"  \n\t\t|__________________|\n\n\t\t\t?=";
                          int val;
                          choice=getche();
                          switch(choice)
                          {
                                        case '1':
                                             cout<<"\n\n\n\tENTER THE VALUE\n\t?=";
                                             cin>>val;
                                            no++;
                                             nodar[no]=ob.noding(elements[no],val);
                                            
                                            if(no==1)
                                             ob.root=*nodar[1];
                                             else
                                             {
                                                 ob.connect_node(nodar[no]);
                                                 }
                                            
                                             break;
                                        case '2':
                                             cout<<"\n\n\nENTER THE VALUE YOU WANT TO DELETE FROM THE TREE\n\n\t?=";
                                             cin>>val;
                                             if(ob.search(val))
                                             {
                                             ob.deleteval(val,&ob.root); cout<<"\n\n\t"<<val<<" deleted successfully\n\n";}
                                             else
                                             cout<<"\n\n\tVALUE YOU ENTERED IS NOT PRESENT IN THE TREE\n\n";
                                             getch();
                                             break;
                                        case '3':
                                             ob.disp_tree(ob.root);
                                             cout<<"\n\n\tPrint the output to file (y/n)\n\n\t?=";
                                             file=getche();
                                             strupr(&file);
                                             if(file=='Y')
                                             {ob.printtofile();
                                             cout<<"\n\n\t\tOUTPUT WROTE TO BST.TXT\n";}
                                             cout<<"\n\n";
                                             break;
                                        case '4':
                                             cout<<"\n\n\n\tENTER THE VALUE YOU WANT TO SEARCH IN THE TREE\n\n\t?=";
                                             cin>>val;
                                             if(ob.search(val)) cout<<"\n\n\t"<<val<<" is present in the tree\n\n\t\tPath to the value:\n";
                                             ob.printpath(val);
                                             cout<<"\n\n\n";
                                             break;
                                        case '5':
                                             break;
                                        default:
                                                cout<<"\n\n\n\t\aINVALID ENTRY";
                                                break;
                                                };
                                                }while(choice!='5');
                         
   cout<<"\n\n\n\t\tTHANK YOU FOR USING THE APPLICATION\a";

     return 0;
     }
