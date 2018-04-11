#include "stdio.h"
#include "syscall.h"
#include "stdlib.h"

int main(char *str, int num){
	
	printf(str);
	printf(" every %d\n", num);
    printf("Waiting Before Testing Join: ");
	int i;
	for(i = 3; i--; i > 0){
		printf("Waiting 3 seconds: %d\n", i);
	}
	exit(3);
}
