#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

char buffer[100];
char buffer2[100];

int main(){
		int returnval;
		int descriptor;
		int descriptor2;
		descriptor2 = creat("test2.txt");
		printf("Descriptor of newly-created test2.txt is: %d\n", descriptor2);
		returnval = close(descriptor2);
		printf("Return value of closing test2.txt is: %d\n", returnval);
	
		descriptor2 = open("test2.txt");
		printf("Descriptor of re-opened test2.txt is: %d\n", descriptor2);
		
		descriptor = open("test.txt");
		printf("Descriptor of newly-opened test.txt is: %d\n", descriptor);
		
		returnval = read(descriptor, buffer, 33);
		printf("Return value of reading test.txt is: %d\n", returnval);
		
		returnval = write(descriptor2, buffer, 33);
		printf("Return value of writing to test2.txt is: %d\n", returnval);
		
		write(1, buffer, 33);
		printf("\n");
		
		returnval = close(descriptor);
		printf("Return value of closing test.txt is: %d\n", returnval);
		
		returnval = unlink("test.txt");
		printf("Return value of unlinking test.txt is: %d\n", returnval);
		
		returnval = unlink("test2.txt");
		printf("Return value of unlinking test2.txt is: %d\n", returnval);
		
		returnval = open("test.txt");
		printf("Descriptor of re-opened test.txt is: %d\n", returnval);
		returnval = open("test2.txt");
		printf("Descriptor of re-opened test2.txt is: %d\n", returnval);
		
		returnval = close(descriptor2);
		printf("Return value of closing test2.txt is: %d\n", returnval);
		
		descriptor2 = open("test2.txt");
		printf("Descriptor of re-opened test2.txt is: %d\n", descriptor2);
		
		printf("Enter any input\n");
		read(0, buffer2, 99);
		write(1, buffer2, 99);
		
		return 0;
}