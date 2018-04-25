#include <stdlib.h>
#include <stdio.h>
#include <string.h>
char str1[100], str2[100];

void Encry()
{
	int key[100];
	char ch,temp;
	int L,i=0,j=0;int t;
	if(getchar()=='\n')
	    temp=' ';
	printf("Input key: ");
	fgets(key,100,stdin);
	L=strlen(key);
	for(t=0;t<L;t++)
	{
		if (key[j%L] >= 'A'&&key[j%L] <= 'Z')
		{
            key[t] = key[j%L] + 32;
        }
		else if (key[j%L] >= 'a'&&key[j%L] <= 'z')
		{
			key[t] = key[j%L] - 32;
		}
		else if(key[j%L] >= ' ' && key[j%L] <= '@')
        {
			key[t] = key[j%L] + 32;
		}
		j++;
		
	}
	
	printf("Input plaintext:");

	while((ch=getchar())!='\n')
	{
		if(ch==' ')
		{
			i++;
			continue;
		}
		if(ch>='a'&&ch<='z')
		{
			str1[i] = ((ch - 'a' + key[j%L] - 'a') % 26 + 'A');
			printf("%c",str1[i]);
			j++;
		}
		if(ch>='A'&&ch<='Z')
		{
			str1[i] = ((ch - 'a' + key[j%L] - 'a') % 26 + 'A');
			printf("%c", str1[i]);	
			j++;   
		}
		if (ch >= ' '&& ch <= '@')
		{
			str1[i] = ((ch - 'a' + key[j%L] - 'a') % 26 + 'A');
			printf("%c", str1[i]);
			j++;
		}
		if(j%L==0)
			printf(" ");
		i++;
	}
	putchar(ch);
}

void Decry()
{
	char key[100];
	char ch,temp;
	int L,i=0,j=0;int t;
	if(getchar()=='\n')
	    temp=' ';
	printf("Input key: ");
	fgets(key,100,stdin);
	L=strlen(key);
		for(t=0;t<L;t++)
	{
		if (key[j%L] >= 'A'&&key[j%L] <= 'Z')
		{
			key[t] = key[j%L] + 32;
		}
		else if (key[j%L] >= 'a'&&key[j%L] <= 'z')
		{
			key[t] = key[j%L] - 32;
		}
		else if (key[j%L] >= ' ' && key[j%L] <= '@')
		{
			key[t] = key[j%L] + 32;
		}
		j++;
	}
	printf("Input ciphertext: ");

	while((ch=getchar())!='\n')
	{
		if(ch==' ')
		{
			i++;
			continue;
		}
		
		if(ch>='A'&& ch<='Z')
		{
			str2[i] = ((ch + 26 - 'A' - key[j%L] + 'a') % 26 + 'a');
			printf("%c", str2[i]);
			j++;
		}
		if (ch >= 'a'&& ch <= 'z')
		{
			str2[i] = ((ch + 26 - 'A' - key[j%L] + 'a') % 26 + 'a');
			printf("%c", str2[i]);
			j++;
		}
		if (ch >= ' '&& ch <= '@')
		{
			str2[i] = ((ch + 26 - 'A' - key[j%L] + 'a') % 26 + 'a');
			printf("%c", str2[i]);
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
	char ch;
	for(;;)
	{
         printf("Please input your option('e/E'To encrypt;'d/D'To decrypt;'q/Q'To quit):");
			ch=getchar();
			if(ch=='e'||ch=='E')
				Encry();
			else if(ch=='d'||ch=='D')
				Decry();
			else if(ch=='q'||ch=='Q')
			   Exit(); 
			else
			{
				printf("Input fault format!");
				putchar(getchar());
				continue;
			}
	}

	return 0;
}
