#include <stdio.h>
#include <string.h>
#include "functions.h"

int main(int argc,char* argv[]){
	if(argv[1] != NULL){
		batch(argv[1]);
	}
	else{
		interactive();
	}
}