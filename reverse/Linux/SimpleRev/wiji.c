#include <stdlib.h>
#include <stdio.h>
#include <string.h>

char str1[100],str2[100];
int key1 = "ADSFK";
char *join1(char *a, char *b);

void Decry()
{
	char key[100];
	char key2 = "NDCLS";
	int len = strlen(key) + strlen(key2) + 1;
	join1(key,len,key2);
	char ch,temp;
	int L,i=0,j=0;int t;
	if(getchar()=='\n')
	    temp=' ';
	fgets(key,100,stdin);
	L=strlen(key);
		for(t=0;t<L;t++)
	{
		if(key[j%L]>='A'&&key[j%L]<='Z')
		{key[t]=key[j%L]+32;}
		j++;
		
	}

		printf("Please input your flag...");
	while((ch=getchar())!='\n')
	{
		if(ch==' ')
		{
			i++;
			continue;
		}
		
		if(ch>='a'&&ch<='z')
		{
            str2[i] = (ch+26-'A'-key[j%L]+'a')%26+'a';
			printf("%c",str2[i]);	
			j++;
		}
        else if(ch>='A'&&ch<='Z')
		{
            str2[i] = (ch+26-'A'-key[j%L]+'a')%26+'a';
			printf("%c",str2[i]);	
			j++;   
		}
		if(j%L==0)
			printf(" ");
		i++;
	}
	putchar(ch);
}

int Exit()
{
   exit(0);
}

int main()
{
	char ch=' ';
	for(;;)
	{
		printf("Welcome to CTF game, please input d/D to start or input q/Q to quit this program.\n");
		ch = getchar();
		if(ch=='d'||ch=='D')
			Decry(key1);
		else if(ch=='q'||ch=='Q')
			Exit(); 
		else
		{
			printf("Input fault format!\n");
			putchar(getchar());
			continue;
		}
	}
	return 0;
}

char *join1(char *a, char *b) {
	char *c = (char *)malloc(strlen(a) + strlen(b) + 1); //局部变量，用malloc申请内存  
	if (c == NULL) exit(1);
	char *tempc = c; //把首地址存下来  
	while (*a != '\0') {
		*c++ = *a++;
	}
	while ((*c++ = *b++) != '\0') {
		;
	}
	//注意，此时指针c已经指向拼接之后的字符串的结尾'\0' !  
	return tempc;//返回值是局部malloc申请的指针变量，需在函数调用结束后free之  
}