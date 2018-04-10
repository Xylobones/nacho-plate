#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(){

	int argNum = 2;
	char *arr[2] = {"Hello", "1"};
	char *proc = "proc3.coff";
	char **paraArr = arr; //Array with valid parameters.
	int procID, result;
	
	printf("Begin TestCase #4: \n");

	procID = exec(proc*, argNum, paraArr**);
	result = join(procID, 3); //Message should print.
	
	//Make sure Join waited and was successful.
	if (result = 1){
		printf("Join was successful and waited appropriately.");
	} else if (result = 0){
		printf("Error with the exit status.");
	} else {
		printf("Something went terribly wrong with the join.");
	}
	
}
