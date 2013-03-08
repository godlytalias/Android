//IMPLEMENTATION OF B-TREE
#include <iostream>
using namespace std;
#include <stdlib.h>

const int MAX = 4 ; //max. no. of keys
const int MIN = 2 ; //min. no. of keys
struct btnode  //stucture of node
{
    int count ;
    int value[MAX + 1] ;  //no:of values=no:of keys+1;
    btnode *child[MAX + 1] ;
} ;
class btree   //defining btree class
{
    private :
        btnode *root ;
    public :
        btree( ) ;
        void insert ( int val ) ;
        int setval ( int val, btnode *n, int *p, btnode **c ) ;
        static int searchnode ( int val, btnode *n, int *pos ) ;
       
        void fillnode ( int val, btnode *c, btnode *n, int k ) ;
        void split ( int val, btnode *c, btnode *n,
                int k, int *y, btnode **newnode ) ;
        void del ( int val ) ;
        //sub-functions for del()
        int delhelp ( int val, btnode *root ) ;
        void clear ( btnode *root, int k ) ;
        void copysucc ( btnode *root, int i ) ;
        void restore ( btnode *root, int i ) ;
        void rightshift ( int k ) ;
        void leftshift ( int k ) ;
        void merge ( int k ) ;
        static void deltree ( btnode *root ) ;
        void show( ) ;
        void search(int key);
        int searchPos(int key,int *key_arr,int n);  //sub-function for search
        static void display ( btnode *root,int blanks ) ;
       
} ;




btree :: btree( )
{
    root = NULL ;
}

void btree::search(int key)
{
int pos, i, n, level=0;
struct btnode *ptr = root;
cout<<"\nSearch path\n";
while (ptr!=NULL)
{
n = ptr->count;
for (i=1; i <= ptr->count; i++)
cout<<ptr->value[i]<<"  ";
cout<<"\n";
pos = searchPos(key, ptr->value, n);
if (pos <= n && key == ptr->value[pos])
{
cout<<key<<" is found in position "<<pos<<" of node at level "<<level;
return;
}
ptr = ptr->child[pos-1];
level++;
}
cout<<"Key "<<key<<" is not available";
}

int btree::searchPos(int key, int *key_arr, int n)
{
int pos=1;
while (pos <= n && key > key_arr[pos])
pos++;
return pos;
}

int btree :: searchnode ( int val, btnode *n, int *pos )
{
    if ( val < n -> value[1] )
    {
        *pos = 0 ;
        return 0 ;
    }
    else
    {
        *pos = n -> count ;
        while ( ( val < n -> value[*pos] ) && *pos > 1 )
            ( *pos )-- ;
        if ( val == n -> value[*pos] )
            return 1 ;
        else
            return 0 ;
    }
}

void btree :: insert ( int val )
{
    int i ;
    btnode *c, *n ;
    int flag ;
    flag = setval ( val, root, &i, &c ) ;
    if ( flag )
    {
        n = new btnode ;
        n -> count = 1 ;
        n -> value[1] = i ;
        n -> child[0] = root ;
        n -> child[1] = c ;
        root = n ;
    }
}
int btree :: setval ( int val, btnode *n, int *p, btnode **ch )
{
    int key ;
    if ( n == NULL )
    {
        *p = val ;
        *ch = NULL ;
        return 1 ;
    }
    else
    {
        if ( searchnode ( val, n, &key ) )
            cout << endl << "Key value already exists." << endl ;
        if ( setval ( val, n -> child[key], p, ch ) )
        {
            if ( n -> count < MAX )
            {
                fillnode ( *p, *ch, n, key ) ;
                return 0 ;
            }
            else
            {
                split ( *p, *ch, n, key, p, ch ) ;
                return 1 ;
            }
        }
        return 0 ;
    }
}


void btree :: fillnode ( int val, btnode *c, btnode *n, int k )
{
    int i ;
    for ( i = n -> count ; i > k ; i-- )
    {
        n -> value[i + 1] = n -> value[i] ;
        n -> child[i + 1] = n -> child[i] ;
    }
    n -> value[k + 1] = val ;
    n -> child[k + 1] = c ;
    n -> count++ ;
}
void btree :: split ( int val, btnode *c, btnode *n,
        int k, int *y, btnode **newnode )
{
    int i, mid ;

    if ( k <= MIN )
        mid = MIN ;
    else
        mid = MIN + 1 ;

    *newnode = new btnode ;

    for ( i = mid + 1 ; i <= MAX ; i++ )
    {
        ( *newnode ) -> value[i - mid] = n -> value[i] ;
        ( *newnode ) -> child[i - mid] = n -> child[i] ;
    }

    ( *newnode ) -> count = MAX - mid ;
    n -> count = mid ;

    if ( k <= MIN )
        fillnode ( val, c, n, k ) ;
    else
        fillnode ( val, c, *newnode, k - mid ) ;

    *y = n -> value[n -> count] ;
    ( *newnode ) -> child[0] = n -> child[n -> count] ;
    n -> count-- ;
}


void btree :: del ( int val )
{
    btnode * temp ;

    if ( ! delhelp ( val, root ) )
        cout << endl << "Value " << val << " not found." ;
    else
    {
        if ( root -> count == 0 )
        {
            temp = root ;
            root = root -> child[0] ;
            delete temp ;
        }
    }
}
int btree :: delhelp ( int val, btnode *root )
{
    int i ;
    int flag ;

    if ( root == NULL )
        return 0 ;
    else
    {
        flag = searchnode ( val, root, &i ) ;
        if ( flag )
        {
            if ( root -> child[i - 1] )
            {
                copysucc ( root, i ) ;
                flag = delhelp ( root -> value[i], root -> child[i] ) ;
                if ( !flag )
                    cout << endl << "Value " << val << " not found." ;
            }
            else
                clear ( root, i ) ;
        }
        else
            flag = delhelp ( val, root -> child[i] ) ;
        if ( root -> child[i] != NULL )
        {
            if ( root -> child[i] -> count < MIN )
                restore ( root, i ) ;
        }
        return flag ;
    }
}


void btree :: clear ( btnode *root, int k )
{
    int i ;
    for ( i = k + 1 ; i <= root -> count ; i++ )
    {
        root -> value[i - 1] = root -> value[i] ;
        root -> child[i - 1] = root -> child[i] ;
    }
    root -> count-- ;
}
void btree :: copysucc ( btnode *root, int i )
{
    btnode *temp = root -> child[i] ;

    while ( temp -> child[0] )
        temp = temp -> child[0] ;

    root -> value[i] = temp -> value[1] ;
}
void btree :: restore ( btnode *root, int i )
{
    if ( i == 0 )
    {
        if ( root -> child [1] -> count > MIN )
            leftshift ( 1 ) ;
        else
            merge ( 1 ) ;
    }
    else
    {
        if ( i == root -> count )
        {
            if ( root -> child[i - 1] -> count > MIN )
                rightshift ( i ) ;
            else
                merge ( i ) ;
        }
        else
        {
            if ( root -> child[i - 1] -> count > MIN )
                rightshift ( i ) ;
            else
            {
                if ( root -> child[i + 1] -> count > MIN )
                    leftshift ( i + 1 ) ;
                else
                    merge ( i ) ;
            }
        }
    }
}
void btree :: rightshift ( int k )
{
    int i ;
    btnode *temp ;

    temp = root -> child[k] ;

    for ( i = temp -> count ; i > 0 ; i-- )
    {
        temp -> value[i + 1] = temp -> value[i] ;
        temp -> child[i + 1] = temp -> child[i] ;
    }

    temp -> child[1] = temp -> child[0] ;
    temp -> count++ ;
    temp -> value[1] = root -> value[k] ;
    temp = root -> child[k - 1] ;
    root -> value[k] = temp -> value[temp -> count] ;
    root -> child[k] -> child [0] = temp -> child[temp -> count] ;
    temp -> count-- ;
}
void btree :: leftshift ( int k )
{
    btnode *temp ;

    temp = root -> child[k - 1] ;
    temp -> count++ ;
    temp -> value[temp -> count] = root -> value[k] ;
    temp -> child[temp -> count] = root -> child[k] -> child[0] ;
    temp = root -> child[k] ;
    root -> value[k] = temp -> value[1] ;
    temp -> child[0] = temp -> child[1] ;
    temp -> count-- ;
    for ( int i = 1 ; i <= temp -> count ; i++ )
    {
        temp -> value[i] = temp -> value[i + 1] ;
        temp -> child[i] = temp -> child[i + 1] ;
    }
}
void btree :: merge ( int k )
{
     int i;
    btnode *temp1, *temp2 ;
    temp1 = root -> child[k] ;
    temp2 = root -> child[k - 1] ;
    temp2 -> count++ ;
    temp2 -> value[temp2 -> count] = root -> value[k] ;
    temp2 -> child[temp2 -> count] = root -> child[0] ;
    for ( i = 1 ; i <= temp1 -> count ; i++ )
    {
        temp2 -> count++ ;
        temp2 -> value[temp2 -> count] = temp1 -> value[i] ;
        temp2 -> child[temp2 -> count] = temp1 -> child[i] ;
    }
    for ( i=k ; i < (root->count) ; i++ )
    {
        root -> value[i] = root -> value[i + 1] ;
        root -> child[i] = root -> child[i + 1] ;
    }
    root -> count-- ;
    delete temp1 ;
}
void btree :: show( )
{
    display ( root,0 ) ;
}
void btree :: display ( btnode *root,int blanks )
{
     int i;
    if ( root != NULL )
    {
       for(i=0;i<blanks;i++)   //putting blanks for distinguishing root & its child
       cout<<" ";
        for ( i = 1 ; i <= root -> count ; i++ )
        {
           
            cout << root -> value[i] << "\t" ;
        }
        cout<<"\n";
        for (i=0;i<=root->count;i++)
        display ( root -> child[i],blanks+8 ) ;
    }
}
void btree :: deltree ( btnode *root )
{
     int i;
    if ( root != NULL )
    {
        for ( i = 0 ; i < root -> count ; i++ )
        {
            deltree ( root -> child[i] ) ;
            delete ( root -> child[i] ) ;
        }
        deltree ( root -> child[i] ) ;
        delete ( root -> child[i] ) ;
    }
}


int main( )
{
       btree b ;
    int choice,key;
    do{
    cout<<"\n_________________________________\n\n\tSELECT YOUR CHOICE\n\n\t\t1. INSERT\n\n\t\t2. DELETE\n\n\t\t3. SEARCH\n\n\t\t4. DISPLAY\n\n\t\t5. EXIT\n\n\n\t\t?=";
    cin>>choice;
    switch(choice)
    {
                  case 1:cout<<"\n\nEnter the key to be inserted  :";
                         cin>>key;
                         b.insert(key);
                         break;
                  case 2:cout<<"\n\nEnter the key to be deleted   :";
                         cin>>key;
                         b.del(key);
                         break;
                  case 3:cout<<"\n\nEnter the key to be searched  :";
                          cin>>key;
                          b.search(key);
                          break;
                  case 4:cout<<"\n\nB-TREE OF ORDER 5\n_________________\n";
                       b.show();
                       cout<<"\n";
                         break;
                 case 5:break;
                   default:cout<<"\nINVALID ENTRY";
                           break;};
                           }while(choice!=5);
                          
                               return 0;
}
