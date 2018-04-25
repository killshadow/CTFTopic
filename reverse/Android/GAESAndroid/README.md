# AES变种题

[toc]

### 一、脱壳

1. 使用jeb打开下载的apk文件，发现代码十分混乱，很明显是加固混淆过的代码，所以需要先将其脱壳。

2. 现在网络上的免费加固服务基本都是第一代，第二代加固，手动脱壳网络上也有很多的教程，这里使用一个现成的工具进行脱壳。

3. DrizzleDumper自动脱壳工具：

   ```powershell
   cd xxxxx/drizzleDumper
   adb push drizzleDumper /data/local/tmp
   adb shell chmod 777 /data/local/tmp/drizzleDumper
   adb shell
   su
   ./data/local/tmp/drizzleDumper com.example.zhang.gaesandroid 3
   ```

   脱下来的dex文件默认文件名为`com.example.zhang.gaesandroid_dumped_88.dex`，导出发现脱壳完毕。

------------

### 二、逻辑分析/算法分析--Decode

1. 脱壳后，将apk文件反编译，可以看到`MainActivity`中主要逻辑为：
   明文密钥:
   `String key="This is a AES-like encryptionalgorithm....Our challenge is to find the plain text of this encrypt messagewith th fixed key. ";`
   输出密文:
   `String encryptedText="eaZwtl5nsHW3ledvoZCdFla5yG13p2Txfq3AN7LEX7s2uK+v7x2Wsz/7jbe0G6R2";`
   寻找`plainText`满足`encrypt(plainText,key)==encryptedText`
   所以，解法为**阅读encrypt方法，写出其逆算法**.

   ​

2. encrypt算法为AES算法，可以在资源文件中找到其流程图aes.png或者从网上搜索到流程，主要逻辑为：
   将明文按16位进行切割（如果不足补0），对每一个16位，将其用类`LightState`进行操作，加上密钥后，调用LightState的方法获得16位密文，拼接在一起获得最终密文，然后base64输出，代码逻辑为：

   ```java
   LightState state=new LightState(inputBytes);
   for(int round=0;round<=10;round++) {
       byteroundKey[]=new byte[16];
       for(int j=0;j<16;j++) {
           roundKey[j]=keyBytes[round*16+j];
           }
       if(round==0){
           state.addRoundKey(roundKey);
       } 
       elseif(round<10) {
           state.substitute();
           state.shiftRows();
           state.mixCloumns();
           state.addRoundKey(roundKey);
       } 
       else {
           state.substitute();
           state.shiftRows();
           state.addRoundKey(roundKey);
       }
   }
   ```

   ​

3. 因此，可以写出逆算法逻辑（或者查到揭秘逻辑）为：

   ```java
   State state=new State(inputBytes, true);
   for(int round=0;round<=10;round++) {
       byteroundKey[]=new byte[16];
       for(intj=0;j<16;j++) {
       roundKey[j]=keyBytes[(10-round)*16+j];
       }
       if(round==0){
           state.addRoundKey(roundKey);
       } 
       elseif(round<10) {
           state.inverseShiftRows();
           state.inverseSubstitute();
           state.addRoundKey(roundKey);
           state.inverseMixCloumns();
       } 
       else {
           state.inverseShiftRows();
           state.inverseSubstitute();
           state.addRoundKey(roundKey);
       }
   }
   ```

   所以问题转化为写出三个核心模块`shiftRows`，`mixCloumns`，`substitute`的逆算法`inverseShiftRows`，`inverseMixCloumns`，`inverseSubstitute`

   ​

4. 根据提示或者阅读`LightState`代码，`shiftRows`和AES完全一致，逆算法逻辑如下：

   ```java
   publicvoid inverseShiftRows() {
       for(inti=0;i<4;i++) {
           bytetemp[]=new byte[4];
           for(intj=0;j<4;j++) {
               temp[j]=_statesi;
           }
           for(intj=0;j<4;j++) {
               _statesi=temp[(4+j-i)%4];
           }
       }
   }
   ```

   `substitute`也没有做改变，其逻辑为对_states4每一个byte，做一个变换得到输出sbox(byte)，因此逆算法在于写出`inverseSbox`方法，这里有三种路径获得：

   - 遍历sbox所有输入，得到所有输出，做成查找表即可

   - 直接查找AES的S盒逆查找表（如<http://www.blogfshare.com/aes-rijndael.html>）

   - `LightState`中有`inverseSbox`方法，可直接调用，最终写法为：

     ```java
     publicvoid inverseSubstitute() {
          for(int i=0;i<4;i++) {
             for(intj=0;j<4;j++) {
                 *statesi=inverseSbox(*statesi);
             }
          }
     }
     ```

     ​

5. `mixCloumns`做了改变，变化在于计算用于求模的多项式从`x8+x4+x3+x+1`变为`x8+x4+x3+x^2+1`，但是逻辑没有改变，因此，正向逻辑为每一列左乘矩阵：

   ```java
   2,3,1,1
   1,2,3,1
   1,1,2,3
   3,1,1,2
   ```

   ​

6. 逆算法同样为左乘逆矩阵(矩阵值可以网上查到)：

   ```java
   e,b,d,9
   9,e,b,d
   d,9,e,b
   b,d,9,e
   ```

   ​

7. 所以需要写出修改后的乘法算法，在`LightState`中给出了没有改动多项式的乘法算法`multiply(inta, int b)`作为参考，可以仿照其样子写，需要改写的是两个多项式乘积后模原来的多项式这部分逻辑改成模新的多项式，逻辑如下：

   ```java
   publicstatic byte multiply_new(int a, int b) {
       if(a<0){
           a+=256;
       }
       if(b<0){
       b+=256;
       }
       intvalues[]=new int[8];
       inttemp=a;
       for(inti=0;i<8;i++) {
           values[i]=temp%2;
           temp/=2;
       }
       intresults[]=new int[11];
       if((b&0x08)>0){
           for(inti=0;i<8;i++) {
               if(values[i]>0){
                   results[i+3]=(results[i+3]+1)%2;
               }
           }
       }

       if((b&0x04)>0){
           for(inti=0;i<8;i++) {
               if(values[i]>0){
                   results[i+2]=(results[i+2]+1)%2;
               }
           }
       }
       if((b&0x02)>0){
           for(inti=0;i<8;i++) {
               if(values[i]>0){
                 results[i+1]=(results[i+1]+1)%2;
               }
           }
       }

       if((b&0x01)>0){
           for(inti=0;i<8;i++) {
               if(values[i]>0){
                   results[i]=(results[i]+1)%2;
               }
           }
       }
       if(results[10]>0){
       results[6]=(results[6]+1)%2;
       results[5]=(results[5]+1)%2;
       results[4]=(results[4]+1)%2;
       results[2]=(results[2]+1)%2;
       }

       if(results[9]>0){
       results[5]=(results[5]+1)%2;
       results[4]=(results[4]+1)%2;
       results[3]=(results[3]+1)%2;
       results[1]=(results[1]+1)%2;
       }

       if(results[8]>0){
       results[4]=(results[4]+1)%2;
       results[3]=(results[3]+1)%2;
       results[2]=(results[2]+1)%2;
       results[0]=(results[0]+1)%2;
       }

       return(byte) (results[7]*128+results[6]*64+results[5]*32+results[4]*16+results[3]*8+results[2]*4+results[1]*2+results[0]);
   }
   ```

   主要修改在最后三个if，即模新的多项式。

   ​

8. 所以列混淆的逆算法可以写为：

   ```java
   publicvoid inverseMixCloumns() {
       for(intj=0;j<4;j++) {
           bytetemp[]=new byte[4];
           for(int i=0;i<4;i++) {
               temp[i]=_statesi;
           }

           _states0=add(multiply_new(temp[0],0xe), multiply_new(temp[1], 0xb), multiply_new(temp[2], 0xd),multiply_new(temp[3], 0x9));

           _states1=add(multiply_new(temp[0],0x9), multiply_new(temp[1], 0xe), multiply_new(temp[2], 0xb),multiply_new(temp[3], 0xd));

           _states2=add(multiply_new(temp[0],0xd), multiply_new(temp[1], 0x9), multiply_new(temp[2], 0xe),multiply_new(temp[3], 0xb));

           _states3=add(multiply_new(temp[0],0xb), multiply_new(temp[1], 0xd), multiply_new(temp[2], 0x9),multiply_new(temp[3], 0xe));
       }
   }
   ```

   ​

9. 至此三个逆算法完成，即可获得逆算法，最终flag为：

   `flag{aes_is_the_best_encryption}`
