#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(){

	int argNum = 2;
	char *arr[2] = {"Hello", "1"};
	char *proc = "proc1.coff";
	char **paraArr = arr; //Array with valid parameters.
	int procID, result;
	
	printF("Begin TestCase #2: \n");

	//test exec, I assume the ID should be 2, given this is the root.
	procID = exec(*proc, argNum, **paraArr);
	if (procID != 2){
		print("%d :Exec was unsuccessful.\n", procID);
	}
	
	//test exec with other methods. (exit is called by proc)
	result = join(procID, 1);

	if (result = 1)
		printF("All went well for proc.\n");
	else if (result = 0)
		printF("Error with the exit status.\n");
	else
		printF("Something went terribly wrong.\n");
	
}
