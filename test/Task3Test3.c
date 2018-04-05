#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(){

	int argNum = 2;
	char *arr[2] = {"Hello", "1"};
	char *proc = "proc2.coff";
	char *proc2 = "Invalid.coff";
	char **paraArr = arr; //Array with valid parameters.
	int procID, result;

	userProcess foreigner = new userProcess(); //Not sure if this would work.

	//ProcessIDs, obtained via exec().
	int valChildID = exec(proc*, argNum, paraArr**);
	int foreignID = foreigner.exec(proc*, argNum, paraArr**);
	int nullID = 12004;

	//exitStatuses, obtained via exit().
	int* valChildPtr = exitStatus.get(valChildID);
	int* invalChildPtr = 3;
	int* foreignPtr = foreigner.exitStatus.get(foreignID);
	int* nullPtr = exitStatus.get(nullID);

	//test join with non-existent child.
	result = join(nullID, nullPtr);
	If (result = -1){
		printf("Return value for joining a non existing process is correct"); 
	} else {
		printf("Return value for joining a non existing process is incorrect");
	}

	//test join with same process twice.
	join(valChildID, valChildPtr);
	result = join(valChildID, valChildPtr);
	If (result = -1){
		printf("Return value for joining the same process is correct"); 
	} else {
		printf("Return value for joining the same process is incorrect");
	}
	
	//test join with a foreign process(join with a child of another process).
	result = join(foreignID, foreignPtr);
	If (result = -1){
		printf("Return value for joining with a foreign child process is correct"); 
	} else {
		printf("Return value for joining with a foreign child process is incorrect");
	}
	
	foreigner.exit(1);
	//test join with a foreign process(join with another parent).
	result = join(foreigner.processID, foreigner.exitStatus.get(foreigner.processID));
	If (result = -1){
		printf("Return value for joining another parent process is correct"); 
	} else {
		printf("Return value for joining another parent process is incorrect");
	}

	//test join with a child possessing bad exit status 
	result = join(valChildID, invalChildPtr);
	If (result = 0){
		printf("Return value for joining a process with a bad exit status is correct"); 
	} else {
		printf("Return value for joining a process with a bad exit status is incorrect"); 
	}


}