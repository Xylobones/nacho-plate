#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(char[] str, int num){
	printf(“%d every“, str + num);
	printf(“.\n“);
	for (int i = 3; i--; i > 0){
		printf(“Wait 3 seconds: %d\n“, i);
	}
	exit(2);
}