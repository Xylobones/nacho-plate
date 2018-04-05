#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(){

	int result;
	int goodLength = 2;
	int negLength = -42;
	int badLength = 3;
	char *gdArr[2] = {"Hello", "1"};
	char *bdPtrArr[2] = {"112", "bob"};
	char *bdArr[2];
	char *procNull = "testNull.coff";
	char *procNcoff = "testNcuff";
	char *proc = "proc1.coff";
	char **goodArr = gdArr; //Array with valid parameters.
	char **badArr = bdArr; //Array with pointers referencing null parameters.
	char **badPtrArr = bdPtrArr;

	//test exec with null file.
	result = exec(procNull, goodLength, goodArr);
	if (result = -1){
		printf("Return value for a null process is correct.\n"); 
	} else {
		printf("Return value for a null process is incorrect.\n");
	}

	//test exec with non .coff file.
	result = exec(procNcoff, goodLength, goodArr);
	if (result = -1){
		printf("Return value for a non .coff file is correct.\n"); 
	} else {
		printf("Return value for a non .coff file is incorrect.\n");
	}

	//test exec with negative number of parameters.
	result = exec(proc, negLength, goodArr);
	if (result = -1){
		printf("Return value for a negative # of parameters is correct.\n"); 
	} else {
		printf("Return value for a negative # of parameters is incorrect.\n");
	}

	//test exec with invalid number of parameters.
	result = exec(proc, badLength, goodArr);
	if (result = -1){
		printf("Return value for an invalid # of parameters is correct.\n"); 
	} else {
		printf("Return value for an invalid # of parameters is incorrect.\n");
	}

	//test exec with bad array of pointers to parameters.
	result = exec(proc, goodLength, badArr);
	if (result = -1){
		printf("Return value for a bad array of pointers is correct.\n"); 
	} else {
		printf("Return value for a bad array of pointers is incorrect.\n");
	}

	//test exec with array of pointers referencing null parameters.
	result = exec(proc, goodLength, badPtrArr);
	if (result = -1){
		printf("Return value for an array of pointers with bad parameters is correct.\n"); 
	} else {
		printf("Return value for an array of pointers with bad parameters is incorrect.\n");
	}

	return 0;
}

