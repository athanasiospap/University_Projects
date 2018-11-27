#include <iostream>
#include <stdlib.h>

using namespace std;

class vector;

class matrix{
    float **a;
    int n;
public:
    matrix();
    matrix(int m);
    int get_n(){return n;}
    float *operator[](int k);
    vector operator!();
    vector operator~();
    void out();
};


class vector{
    float *v;
    int n;
public:
    vector(){}
    vector(int k);
    void set(int i,float t);
    void set(float p,int j);
    int operator>(vector w);
    vector operator=(matrix x);
    vector operator-(vector x);
    float operator++();
    void out();
};

matrix::matrix(int m)
{
    int i,j;
    n=m;
    a=(float **)malloc(n*sizeof(float *));
    if (a==NULL){
        cout<<"den yparxei arketh mnhmh";
        exit(1);
    }
    for(i=0;i<n;i++){
        a[i]=(float *)malloc(n*sizeof(float));
        if (a[i]==NULL){
            cout<<"den yparxei arketh mnhmh";
            exit(1);
        }
    }
    cout<<"dwse stoixeia toy pinaka";
    for (i=0;i<n;i++){
        for (j=0;j<n;j++){
           cin>>a[i][j];
        }
    }
}

float *matrix::operator[](int k)
{
    return a[k];
}

vector matrix::operator!()
{
    vector t(n);
    int i,j;
    float a_row,a_column;
    for (i=0;i<n;i++){
        a_row=0;
        a_column=0;
        for (j=0;j<n;j++){
            if (i!=j)
                a_row+=a[i][j]<0 ? -a[i][j] : a[i][j];
        }
        for (j=0;j<n;j++){
            if (i!=j)
                a_column+=a[j][i]<0 ? -a[i][j] : a[i][j];
        }
        if(a_row<a_column)
            t.set(i,a_row);
        else
            t.set(i,a_column);
    }
    return t;
}

vector matrix::operator~()
{
    vector p(n);
    int i,j;
    float a_row,a_column;
    for (i=0;i<n;i++){
        a_row=0;
        a_column=0;
        for (j=0;j<n;j++){
            if (i!=j)
                a_row+=a[i][j]<0 ? -a[i][j] : a[i][j];
        }
        for (j=0;j<n;j++){
            if (i!=j)
                a_column+=a[j][i]<0 ? -a[i][j] : a[i][j];
        }
        if(a_row<a_column)
            p.set(a_row,j);
        else
            p.set(a_column,j);
    }
    return p;
}

void matrix::out()
{
    int i,j;
    for(i=0;i<n;i++){
        for(j=0;j<n;j++)
            cout<<a[i][j];
    }
}

vector::vector(int k)
{
    int i;
    n=k;
    v=(float *)malloc(n*sizeof(float));
    if(v==NULL){
        cout<<"den yparxei diathesimh mnhmh\n";
        exit(1);
    }
}

void vector::set(int i,float t)
{
    v[i]=t;
}


void vector::set(float p,int j)
{
    v[j]=p;
}

int vector::operator>(vector w)
{
    int i,fl=1;
    if(n!=w.n){
        cout<<"adynath sygkrish\n";
        exit(1);
    }
    for(i=0;i<n;i++){
        if(v[i]<=w.v[i]){
        fl=0;
        return fl;
        }
    }
    return fl;
}

vector vector::operator=(matrix x)
{
    int i;
    n=x.get_n();
    v=(float *)malloc(n*sizeof(float));
    if(v==NULL){
        cout<<"den yparxei mnhmh\n";
        exit(1);
    }
    for(i=0;i<n;i++){
        v[i]=x[i][i]<0 ? -x[i][i] : x[i][i];
        return *this;
    }
}

vector vector::operator-(vector x)
{
    int i;
    vector t(n);
    if(n!=x.n){
        cout<<"den yparxei mnhmh\n";
        exit(1);
    }
    for(i=0;i<n;i++){
        t.v[i]=v[i]-x.v[i];
        return t;
    }
}

float vector::operator++()
{
    int i;
    float sum=0;
    for(i=0;i<n;i++){
        sum+=v[i];
        return sum;
    }
}

void vector::out()
{
    int i;
    for(i=0;i<n;i++){
    cout<<v[i];
    }
}

int main()
{
    int i,n;
    vector v,d,df;
    matrix a(n);
    cout<<"dwse arithmo grammwn pinaka a = ? ";
    cin>>n;
    d=a;
    v=!a;
    if(d>v){
        cout<<"o pinakas a einai diagwnios yperterwn\n";
        df=d-v; //Διαφορά απόλυτων τιμών πίνακα
        cout<<"diafores apolytwn timwn\n";
        df.out();
        cout<<"athroisma diaforwn"<<++df;
    }
    else
        cout<<"o pinakas den einai daigwnios yperterwn\n";
}
