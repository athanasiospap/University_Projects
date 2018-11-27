#include <iostream>
#include <stdlib.h>

using namespace std;


class product{
    int code,apothema,offer,k;
    float price,kg_price,kg_apothema;
  public:
      friend float sale(product a);
      product();
      product(int a,int b,float c);
      product(int a,int b,float c,int d);
      product(int a,float b,float c);
      int get_code(){return code;}
      int get_apothema(){return apothema;}
      float get_price(){return price;}
      int get_offer(){return offer;}
      float get_kg_price(){return kg_price;}
};

product::product(int a,int b,float c)
{
    k=1;
    cout<<"dwse kwdiko = ? \n";
    cin>>code;
    cout<<"dwse arithmo temaxiwn = ";
    cin>>apothema;
    cout<<"dwse timh / temaxio = ?";
    cin>>price;
}

product::product(int a,int b,float c,int d)
{
    k=2;
    cout<<"dwse kwdiko = ? \n";
    cin>>code;
    cout<<"dwse arithmo temaxiwn = ";
    cin>>apothema;
    cout<<"dwse timh / temaxio = ?";
    cin>>price;
    cout<<"dwse prosfora ";
    cin>>offer;
}

product::product(int a,float b,float c)
{
    k=3;
    cout<<"dwse kwdiko = ? \n";
    cin>>code;
    cout<<"dwse arithmo temaxiwn = ";
    cin>>kg_apothema;
    cout<<"dwse timh / kilo = ?";
    cin>>kg_price;
}

float sale(product a)
{
    float l,s,timh;
    cout<<"dwse aithmo temiaxiwn h baros ";
    cin>>l;
    s = a.get_apothema() - l;
    cout<<s;
    timh = a.get_price()*s;
    return timh;
}

int main()
{
    int ar_pr,i,k,thesh,code,offer,quantity;
    float apothema,price,kg_price,kg_apothema,sum=0,timh;
    product *p;

    cout<<"dwse arithmo proiontwn = ";
    cin>>ar_pr;

    if((product *)malloc(ar_pr*sizeof(product))==NULL){
        cout <<"Provlima me th mnhmh/n";
        exit(1);
    }
    else{

    for (i=0;i<ar_pr;i++){
        cout<<"dwse katigoria prointwn ";
        cin>>k;
        cout<<"dwse kwdiko,apothema,timh ";
        cin>>code>>apothema>>price;
        switch(k){
            case 1:
                p[i]=product(code,apothema,price);
            case 2:
                p[i]=product(code,apothema,price,offer);
            case 3:
                p[i]=product(code,kg_apothema,kg_price);
        }
    }
    for (;;){
        cout<< "dwse ton kwdiko tou proiontos: ";
        cin>> code;
        cout<<"dwse ton arithmo twn temaxiwn/kg tou proiontos pou agorases: ";
        cin>> quantity;
        for (i=0;i<ar_pr;i++){
            if (code=p[i].get_code()){
                    sale(p[i]);
                    break;
            }
        }
        sum += timh;
    }
    }
}
