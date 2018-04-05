#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(){

	int argNum = 2;
	char *arr[2] = {"Hello", "1"};
	char *proc = "proc1.coff";
	char **paraArr = arr; //Array with valid parameters.
	int procID, result;

	//test a non-root user process
	procID = exec(proc*, argNum, paraArr**);

	result = join(procID, 2);
	if (result = 1){
		printf("proc1 exited successfully with a good exit status.");
	} else if (result = 0) {
		printf("proc1 exited but its exit status is incorrect.");
	} else {
		printf("Something went terribly wrong.");
	}
	
	//Assuming this is the root, halt the machine via exit().
	printf("The machine should halt here.\n");
	exit(0);
	
}
