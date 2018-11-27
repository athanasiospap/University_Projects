#include <iostream>

using namespace std;

class device{
protected:
    static float distance,power;
    static int n;
    static device **d,**type;
public:
    device();
    device(char *s);
    virtual float return_distance()=0;
    virtual float return_power()=0;
};

device **device::d;
float device::distance=0;
float device::power=0;

device::device()
{
    cout<<"\nobject created\n";
}

device::device(char *s)
{
    cout<<"type"<<s<<"created";
}

class device1:public device{
    float power,mhkos;
public:
    device1();
    device1(char *s);
    void createUnits();
    float return_distance();
    float return_power();
}dev1("syskeyh1");


device1::device1()
{
    cout<<"dwse isxus gia device1";
    cin>>power;
    cout<<"dwse mhkos gia device1";
    cin>>mhkos;
}


void device1::createUnits()
{
    int n,i;
    cin>>n;
    for(i=0;i<n;i++){
        device1 temp;
    }
}

device1::device1(char *s)
{
    createUnits();
}

float device1::return_distance()
{
    return distance;
}

float device1::return_power()
{
    return power;
}


class device2:public device{
    float power_kin,power_ant,power,mhkos,c;
public:
    device2();
    device2(char *s);
    void createUnits();
    float return_distance();
    float return_power();
}dev2("syskeyh2");


device2::device2()
{
    cout<<"dwse isxu kinithra\n";
    cin>>power_kin;
    cout<<"dwse isxu antikstasewn\n";
    cin>>power_ant;
    power=power_kin+power_ant;
    cout<<"dwse mhkos gia device2";
    cin>>mhkos;
}

void device2::createUnits()
{
    int n,i;
    cin>>n;
    for(i=0;i<n;i++){
        device2 temp;
    }
}

device2::device2(char *s)
{
    createUnits();
}


float device2::return_distance()
{
    return distance;
}

float device2::return_power()
{
    return power;
}

float get_total_P(device **d,int n,int *A,float *S,float V)
{
    int i,*tem;
    float total_power,I,c;
    for (i=0;i<n;i++){
        *S+=d[i]->return_distance();
        *A+=d[i]->return_power();
        I=A[i]/V;
        if (I<10){
            tem[0]+=1;
        }
        else if(I<16){
            tem[1]+=1;
        }
        else if(I<20){
            tem[2]+=1;
        }
        else if(I<=25){
            tem[3]+=1;
        }
        else {
            cout<<"Den epitrepetai h timh tou reymatos na yperbainei ta 25A\n";
            continue;
        }

    }
    cout<<"dwse syntelesth c gia to device2";
    cin>>c;
    total_power=A[1]*c*A[2];
    return total_power;
}

int main()
{
    int R[]={0,0,0,0},i,tem[3];
    float S[]={0,0,0,0},power,V;
    device1 d1;
    device2 d2;
    device *d[2];
    d[0] = &d1;
    d[1] = &d2;
    cout<<"dwse tash V=?";
    cin>>V;
    power = get_total_P(d,2,R,S,V);
    cout <<"Synolikh isxys:"<<power;
    for(i=0;i<4;i++){
        cout <<"R"<<i+1<<" "<<R[i];
    }
    for(i=0;i<4;i++){
        cout <<"S"<<i+1<<" "<<S[i];
        cout<<"temaxia"<<i+1<<" "<<tem[i];
    }
    return 0;
    
}
