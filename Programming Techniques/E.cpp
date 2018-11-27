#include <iostream>
#include <stdlib.h>

using namespace std;

class material{
    float amount,weight;
public:
    material();
    float get_vol(){return amount/weight;}
    void set_amount(float V){amount=V*weight;}
};

material::material()
{
    cout<<"\ndwse posothta";
    cin>>amount;
    cout<<"\ndwse eidiko baros";
    cin>>weight;
}



class product{
public:
    product();
    virtual float calk_vol()=0;
    virtual float get_pieces()=0;
};

product **P;
int Np=0;

product::product()
{
    if(Np==0){
        P=(product **)malloc(sizeof(product *));
        if(P==NULL){
            cout<<"\n den yparxei mnhmh 1";
            exit(1);
        }
        P[0]=this;
        Np++;
    }
    else{
        Np++;
        P=(product **)realloc(P,Np*sizeof(product *));
        if(P==NULL){
            cout<<"\n den yparxei mnhmh 2";
            exit(1);
        }
        P[Np-1]=this;
    }
}

void production(product **p,int n_p,material *m,int n_m)
{
    int i,j,pieces,tp,sum=0;
    float m_vol,prod_vol,pv,p_vol;
    for (i=0;i<n_p;i++){
        pieces=p[i]->get_pieces();
        p_vol=p[i]->calk_vol();
        prod_vol=pieces*p_vol;
        for(j=0;j<n_m;j++){
            m_vol=m[j].get_vol();
            if(m_vol==0)continue;
            if(p_vol<=m_vol){
                sum+=pieces;
                m_vol-=prod_vol;
                m[j].set_amount(m_vol);
                break;
            }
            else{
                tp=m[j].get_vol()/pv;
                if(tp==0)continue;
                sum+=pieces;
                m_vol-=tp*pv;
                m[j].set_amount(m_vol);
                pieces-=tp;
                prod_vol=pieces*pv;
            }
        }
    cout<<sum;
    }
}

class product1:public product{
    float R,h,ar_tem,pieces;
public:
    product1();
    float calk_vol();
    float get_pieces(){return pieces;}
};

product1::product1()
{
    cout<<"\ndwse aktina R = ?";
    cin>>R;
    cout<<"\ndwse ypsos h = ?";
    cin>>h;
    cout<<"\ndwse arithmo temaxiwn ";
    cin>>pieces;
}

float product1::calk_vol()
{
    float vol;
    vol=R*h*3.14;
    return vol;
}



class product2:public product{
    float R,h,ar_tem,pieces;
public:
    product2();
    float calk_vol();
    float get_pieces(){return pieces;}
};

product2::product2()
{
    cout<<"\ndwse aktina R = ?";
    cin>>R;
    cout<<"\ndwse ypsos h = ?";
    cin>>h;
    cout<<"\ndwse arithmo temaxiwn ";
    cin>>pieces;
}

float product2::calk_vol()
{
    float vol;
    vol=h*h*h;
    return vol;
}


int main()
{
   material M[5];
   product1 P1[4];
   product2 P2[5];
   production(P,Np,M,5);
}
