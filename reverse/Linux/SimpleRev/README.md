## SimpleRev

简书链接：https://www.jianshu.com/p/e6928c535409

1.  一开始现在Linux下用`file`命令看看是什么文件:
![](http://upload-images.jianshu.io/upload_images/8343187-9109b2776f353eb4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


2.  然后执行一下程序, 一看究竟, 发现执行不了`Segmentation fault (core dumped)`段错误:
![](http://upload-images.jianshu.io/upload_images/8343187-dfaa90aa935e2b40.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


3.  用IDA打开, 从程序初始化开始, 在_init_proc中出现了`sp-analysis failed`的错误, 通过对比两个rsp可知, 是因为函数开头的rsp被修改了:
![](http://upload-images.jianshu.io/upload_images/8343187-95ca48c245c6a597.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


4.  patch之后, 能正常运行:
![](http://upload-images.jianshu.io/upload_images/8343187-c23a06ca2411f1a1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


5.  可以大致猜测, 这是一道分析算法的逆向题:
![](http://upload-images.jianshu.io/upload_images/8343187-102e74e55d4958d8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


6.  再回到IDA，看看特征函数：
![](http://upload-images.jianshu.io/upload_images/8343187-6cc66326d152d153.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


7.  通过下面反汇编内容，就已经可以看出key和text分别是`ADSFKSLCDN`和`killshadow`：
![](http://upload-images.jianshu.io/upload_images/8343187-8fdcbd92681c1068.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![](http://upload-images.jianshu.io/upload_images/8343187-e451672881343c95.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


8.  再看看函数逻辑：
![](http://upload-images.jianshu.io/upload_images/8343187-3ca347f955d23e73.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)可以看到，首先是对key操作，然后是跟text比较：![](http://upload-images.jianshu.io/upload_images/8343187-6290629475e5388b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


9.  写出解密算法即可解出flag：

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


10.  最后解得flag为：`KLDQCUDFZO`
![](http://upload-images.jianshu.io/upload_images/8343187-0043240ab8733179.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
