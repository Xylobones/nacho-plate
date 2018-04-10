#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(char[] str, int num){
	
	int argNum = 2;
	char *arr[2] = {"Test Case2: Hello", "1"};
	char *proc = "proc1.coff";
	char **paraArr = arr; //Array with valid parameters.
	int procID, result;

	//create child
	procID = exec(*proc, argNum, **paraArr);
	printf(“ProcessID = %d\n“, procID);
	printf(str);
	printf(“ every %d\n“, num);
	exit(2);
}
