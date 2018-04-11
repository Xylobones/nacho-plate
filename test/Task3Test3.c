#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(){

	int argNum = 2;
	int errorStat = 2;
	char *arr[2] = {"Hello", "1"};
	char *proc = "proc2.coff";
	char *proc2 = "Invalid.coff";
	char **paraArr = arr; //Array with valid parameters.
	int procID, result;

	//ProcessIDs & Misc, obtained via exec().
	int valChildID = exec(proc, argNum, paraArr);
	int nullID = 12004;
	int* invalChildPtr = 3;
	
	printf("Begin TestCase #3: \n");

	//test join with non-existent child.
	result = join(nullID, 0);
	If (result = -1){
		printf("Return value for joining a non existing process is correct"); 
	} else {
		printf("Return value for joining a non existing process is incorrect");
	}

	//test join with same process twice.
	join(valChildID, 2);
	result = join(valChildID, errorStat);
	If (result = -1){
		printf("Return value for joining the same process is correct"); 
	} else {
		printf("Return value for joining the same process is incorrect");
	}
	
	//test join with a foreign process(join with a child of another process).
	result = join(3, errorStat);
	If (result = -1){
		printf("Return value for joining with a foreign child process is correct"); 
	} else {
		printf("Return value for joining with a foreign child process is incorrect");
	}

	//test join with a child possessing bad exit status 
	result = join(valChildID, invalChildPtr);
	If (result = 0){
		printf("Return value for joining a process with a bad exit status is correct"); 
	} else {
		printf("Return value for joining a process with a bad exit status is incorrect"); 
	}


}
