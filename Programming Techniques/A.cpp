#include <iostream>
#include <process.h>
#include <string.h>
#include <stdlib.h>

using namespace std;

class member {
    private:
        int n,ar_fr,id,fr[10];
        char name[20],login[20],password[20];
        int id_fr[10];
    public:
        member();
        void add_fr(int n);
        void rem_fr(int n);
        void set_id(int n){id=n;}
        int get_id(){return id;}
        char *get_name(){return name;}
        char *get_login(){return login;}
        char *get_password(){return password;}
        int *get_friends(int &fn){fn=ar_fr;return fr;}
};

member::member()
{
    char password1[20],password2[20];
    cout<<"Onoma = ? ";
    cin>>name;
    cout<<"Login name = ? ";
    cin>>login;
    cout<<"Password = ? ";
    cin>>password1;
    cout<<"Dwse password 3ana = ? ";
    cin>>password2;
    if(strcmp(password1,password2)){
        cout<<"\nLathos kwdikos\n";
        id=1;
        return;
        }
    strcpy(password,password1);
    ar_fr=0;
    id=0;
}


void member::add_fr(int fr_id)
{
    if(ar_fr<10){
        id_fr[ar_fr]=fr_id;
        ar_fr++;
        }
    else{
        cout<<"lista filwn plhrhs\n";
        return;
        }
}

void member::rem_fr(int fr_id)
{
    int i;
    for(i=0;i<ar_fr;i++)
        if(id_fr[i]==fr_id){
            ar_fr--;
            if(i==ar_fr)
                return;
        id_fr[i]=id_fr[ar_fr];
    return;
    }
}


int main()
{
   int i,n=0,k,id,my_id=0,fl,fn,*fr;
   char name[20],login[20],password[20];
   member *p;

   for(;;)
   {
      member new_member;
      if(new_member.get_id())
          continue;
        if((!strcmp("admin",new_member.get_login()))&&(!strcmp("apass",new_member.get_password())))
          break;
        fl=0;
        for(i=0;i<n;i++){
           if(strcmp(p[i].get_login(),new_member.get_login()))
             continue;
        cout<<"\nThis login name already exists!"<<new_member.get_login()<<"\n";
        fl=1;
        break;
    }

    if(fl)
      continue;
      if(n==0)
        p=(member *)malloc(sizeof(member));
      else
        p=(member *)realloc(p,(n+1)*sizeof(member));
        p[n]=new_member;
        p[n].set_id(n+1);
        n++;
        cout<<"\nMpeikate sthn selida "<<p[n-1].get_name()<<"\n\n";
   }

    cout<<"\nSynolika "<<n<<" atoma\n\n";
    for(i=0;i<n;i++)
        cout<<p[i].get_name()<<"\n";

    for(;;){
        if (my_id!=0){
        cout<<"\nDwse login name = ";
        cin>>login;
        cout<<"\nDwse password = ";
        cin>>password;
        for(i=0;i<n;i++){
            if(strcmp(p[i].get_login(),login))continue;
            if(strcmp(p[i].get_password(),password))continue;
            my_id=p[i].get_id();
            break;
            }
        }
        cout<<"1 gia anazhthsh melous\n";
        cout<<"2 gia kataxvrhsh filoy\n";
        cout<<"3 gia diagrafh filoy\n";
        cout<<"4 gia typwsh filwn\n";
        cout<<"Allo plhktro gia e3odo\n";
        cin>>k;
        switch(k){
        case 1:
            fl = 1;
            cout<<"Dwse onoma filoy ";
            cin>>name;
            for(i=0;i<n;i++){
            if(strcmp(p[i].get_name(),name))continue;
            cout<<name,p[i].get_id();
            fl=0;break;
            }
            if (fl)
                cout<<"den einai grammenos";
                break;
        case 2:
            cout<<"Dwse id";
            cin>>id;
            p[my_id-1].add_fr(id);
            cout<<p[id-1].get_name()<<" kataxwrhthke ws filos ";
            break;
        case 3:
            cout<<"Dwse id gia diagrafh ";
            cin>>id;
            fr=p[my_id-1].get_friends(fn);
            for(i=0;i<fn;i++){
                if(id==fr[i]){
                p[my_id-1].rem_fr(id);
                fl=0;;
                break;
                }
                }
        case 4:
            fr=p[my_id-1].get_friends(fn);
            for(i=0;i<fn;i++)
                cout<<p[fr[i]-1].get_name()<<"\n";
                continue;
                default:
                my_id=0;
}
    }
}
