#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"
//The goal of this test is to ensure that file system calls fail gracefully with bad input. Assumes that "test.txt" does not exist.
char emptyBuffer[0];
char buffer[100];
int main(){
	int returnval;
	int descriptor;
	descriptor = creat(0);
	printf("Descriptor of newly-created file with NULL name is: %d\n", descriptor);
	
	descriptor = open(0);
	printf("Descriptor of opened file with NULL name is: %d\n", descriptor);
	
	descriptor = open("test.txt");
	printf("Descriptor of opened file that does not exist is: %d\n", descriptor);
	
	returnval = read(3, buffer, 1);
	printf("Return value of reading file that does not exist is: %d\n", returnval);
	
	
	returnval = read(0, buffer, 0);
	printf("Return value of reading file with count = 0 is: %d\n", returnval);
	
	returnval = write(3, buffer, 1);
	printf("Return value of writing to file that does not exist is: %d\n", returnval);
	
	returnval = write(1, buffer, 0);
	printf("Return value of writing to file with count = 0 is: %d\n", returnval);
	
	returnval = close(3);
	printf("Return value of closing file that does not exist is: %d\n", returnval);
	
	returnval = unlink("test.txt");
	printf("Return value of unlinking file that does not exist is: %d\n", returnval);
	
	printf("Opening files until max is reached.\n");
	do{
		descriptor = creat("out");
		printf("File with descriptor %d opened.\n", descriptor);
	} while (descriptor != -1);
	
}