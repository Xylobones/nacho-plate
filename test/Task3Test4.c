#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(){

	int argNum = 2;
	char *arr[2] = {"Hello", "1"};
	char *proc = "proc2.coff";
	char **paraArr = arr; //Array with valid parameters.
	int procID, result;

	procID = exec(proc*, argNum, paraArr**);
	int *stat = exitStatus.get(procID);

	result = join(procID, *stat); //message should print.
	
	//Make Sure Join Waited.
	if (result = 1){
		printf("Join was successful and waited appropriately.");
	} else if (result = 0){
		printf("Error with the exit status.");
	} else {
		printf("Something went terribly wrong with the join.");
	}
	
}