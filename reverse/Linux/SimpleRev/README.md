## SimpleRev

1. 一开始现在Linux下用`file`命令看看是什么文件:

   ![Snipaste_2018-02-05_11-56-45](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_11-56-45.jpg)

2. 然后执行一下程序, 一看究竟, 发现执行不了`Segmentation fault (core dumped)`段错误:

   ![Snipaste_2018-02-05_12-00-44](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_12-00-44.jpg)

3.  用IDA打开, 从程序初始化开始, 在_init_proc中出现了`sp-analysis failed`的错误, 通过对比两个rsp可知, 是因为函数开头的rsp被修改了:

   ![Snipaste_2018-02-05_13-19-21](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_13-19-21.jpg)

4. patch之后, 能正常运行:

   ![Snipaste_2018-02-05_13-24-02](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_13-24-02.jpg)

5. 可以大致猜测, 这是一道分析算法的逆向题:

   ![Snipaste_2018-02-05_13-24-57](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_13-24-57.jpg)

6. 再回到IDA，看看特征函数：

   ![Snipaste_2018-02-05_13-53-05](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_13-53-05.jpg)

7. 通过下面反汇编内容，就已经可以看出key和text分别是`ADSFKSLCDN`和`killshadow`：

   ![Snipaste_2018-02-05_13-41-24](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_13-41-24.jpg)

   ![Snipaste_2018-02-05_13-42-33](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_13-42-33.jpg)

8. 再看看函数逻辑：

   ![Snipaste_2018-02-05_13-44-56](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_13-44-56.jpg)

   可以看到，首先是对key操作，然后是跟text比较：

   ![Snipaste_2018-02-05_13-46-51](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_13-46-51.jpg)

9. 写出解密算法即可解出flag：

   ```c
   void Encry()
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
   		if (key[j%L] >= 'A'&&key[j%L] <= 'Z' || key[j%L] >= 'a'&&key[j%L] <= 'z')
   		{
               key[t] = key[j%L] + 32;
           }
   		else if(key[j%L] >= ' ' && key[j%L] <= '@')
           {
   			key[t] = key[j%L] + 32;
   		}
   		j++;
   		
   	}

   	while((ch=getchar())!='\n')
   	{
   		if(ch==' ')
   		{
   			i++;
   			continue;
   		}
   		if(ch>='a'&&ch<='z')
   		{
   			str1[i] = (ch - 'a' + key[j%L] - 'a') % 26 + 'A';
   			printf("%c",str1[i]);	                                                                    
   			j++;
   		}
   		if(ch>='A'&&ch<='Z')
   		{
   			str1[i] = (ch - 'a' + key[j%L] - 'a') % 26 + 'A';
   			printf("%c", str1[i]);	
   			j++;   
   		}
   		if (ch >= ' '&& ch <= '@')
   		{
   			str1[i] = (ch - 'a' + key[j%L] - 'a') % 26 + 'A';
   			printf("%c", str1[i]);
   			j++;
   		}
   		if(j%L==0)
   			printf(" ");
   		i++;
   	}
   	putchar(ch);
   }
   ```

10. 最后解得flag为：`KLDQCUDFZO` 

   ![Snipaste_2018-02-05_13-50-58](C:\Users\Killshadow\Desktop\blog\topic\image\SimpleRev\Snipaste_2018-02-05_13-50-58.jpg)

