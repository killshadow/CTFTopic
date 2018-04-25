package com.zhang.gaes;

public class LightState {

    protected byte[][] _states;

    public LightState(byte[] states) {
        _states=new byte[4][4];
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                _states[j][i]=states[4*i+j];
            }
        }
    }

    public byte value(int i, int j) {
        return _states[i][j];
    }

    public void shiftRows() {
        for(int i=0;i<4;i++) {
            byte temp[]=new byte[4];
            for(int j=0;j<4;j++) {
                temp[j]=_states[i][j];
            }
            for(int j=0;j<4;j++) {
                _states[i][j]=temp[(j+i)%4];
            }
        }
    }

    public void addRoundKey(byte[] key) {
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                _states[i][j]^=key[4*j+i];
            }
        }
    }

    public static byte add(byte a, byte b) {
        return (byte) (a^b);
    }

    public static byte add(byte a, byte b, byte c, byte d) {
        return (byte) (a^b^c^d);
    }

    public static byte multiply2(byte a) {
        if(a>=0) {
            return (byte) (a<<1);
        } else {
            return (byte) (a<<1^0x1d);
        }
    }
    public static byte multiply3(byte a) {
        return add(a, multiply2(a));
    }

    public void mixCloumns() {
        for(int j=0;j<4;j++) {
            byte temp[]=new byte[4];
            for(int i=0;i<4;i++) {
                temp[i]=_states[i][j];
            }
            _states[0][j]=add(multiply2(temp[0]), multiply3(temp[1]), temp[2], temp[3]);
            _states[1][j]=add(temp[0], multiply2(temp[1]), multiply3(temp[2]), temp[3]);
            _states[2][j]=add(temp[0], temp[1], multiply2(temp[2]), multiply3(temp[3]));
            _states[3][j]=add(multiply3(temp[0]), temp[1], temp[2], multiply2(temp[3]));
        }
    }

    public static byte inverseSbox(byte a) {
        int[] INVSBOX=new int[] {
                0x52,0x09,0x6a,0xd5,0x30,0x36,0xa5,0x38,0xbf,0x40,0xa3,0x9e,0x81,0xf3,0xd7,0xfb, /*0*/
                0x7c,0xe3,0x39,0x82,0x9b,0x2f,0xff,0x87,0x34,0x8e,0x43,0x44,0xc4,0xde,0xe9,0xcb, /*1*/
                0x54,0x7b,0x94,0x32,0xa6,0xc2,0x23,0x3d,0xee,0x4c,0x95,0x0b,0x42,0xfa,0xc3,0x4e, /*2*/
                0x08,0x2e,0xa1,0x66,0x28,0xd9,0x24,0xb2,0x76,0x5b,0xa2,0x49,0x6d,0x8b,0xd1,0x25, /*3*/
                0x72,0xf8,0xf6,0x64,0x86,0x68,0x98,0x16,0xd4,0xa4,0x5c,0xcc,0x5d,0x65,0xb6,0x92, /*4*/
                0x6c,0x70,0x48,0x50,0xfd,0xed,0xb9,0xda,0x5e,0x15,0x46,0x57,0xa7,0x8d,0x9d,0x84, /*5*/
                0x90,0xd8,0xab,0x00,0x8c,0xbc,0xd3,0x0a,0xf7,0xe4,0x58,0x05,0xb8,0xb3,0x45,0x06, /*6*/
                0xd0,0x2c,0x1e,0x8f,0xca,0x3f,0x0f,0x02,0xc1,0xaf,0xbd,0x03,0x01,0x13,0x8a,0x6b, /*7*/
                0x3a,0x91,0x11,0x41,0x4f,0x67,0xdc,0xea,0x97,0xf2,0xcf,0xce,0xf0,0xb4,0xe6,0x73, /*8*/
                0x96,0xac,0x74,0x22,0xe7,0xad,0x35,0x85,0xe2,0xf9,0x37,0xe8,0x1c,0x75,0xdf,0x6e, /*9*/
                0x47,0xf1,0x1a,0x71,0x1d,0x29,0xc5,0x89,0x6f,0xb7,0x62,0x0e,0xaa,0x18,0xbe,0x1b, /*a*/
                0xfc,0x56,0x3e,0x4b,0xc6,0xd2,0x79,0x20,0x9a,0xdb,0xc0,0xfe,0x78,0xcd,0x5a,0xf4, /*b*/
                0x1f,0xdd,0xa8,0x33,0x88,0x07,0xc7,0x31,0xb1,0x12,0x10,0x59,0x27,0x80,0xec,0x5f, /*c*/
                0x60,0x51,0x7f,0xa9,0x19,0xb5,0x4a,0x0d,0x2d,0xe5,0x7a,0x9f,0x93,0xc9,0x9c,0xef, /*d*/
                0xa0,0xe0,0x3b,0x4d,0xae,0x2a,0xf5,0xb0,0xc8,0xeb,0xbb,0x3c,0x83,0x53,0x99,0x61, /*e*/
                0x17,0x2b,0x04,0x7e,0xba,0x77,0xd6,0x26,0xe1,0x69,0x14,0x63,0x55,0x21,0x0c,0x7d  /*f*/
        };
        return (byte)INVSBOX[(a+256)%256];
    }

    public static byte multiply(int a, int b) {
        if(a<0) {
            a+=256;
        }
        if(b<0) {
            b+=256;
        }
        int avalues[]=new int[8];
        int bvalues[]=new int[8];
        for(int i=0;i<8;i++) {
            avalues[i]=a%2;
            a/=2;
        }
        for(int i=0;i<8;i++) {
            bvalues[i]=b%2;
            b/=2;
        }
        int results[]=new int[16];
        for(int j=7;j>=0;j--) {
            if(bvalues[j]>0) {
                for(int i=0;i<8;i++) {
                    if(avalues[i]>0) {
                        results[i+j]=(results[i+j]+1)%2;
                    }
                }
            }
        }


        if(results[15]>0) {
            results[5]=(results[5]+1)%2;
            results[3]=(results[3]+1)%2;
            results[2]=(results[2]+1)%2;
            results[1]=(results[1]+1)%2;
            results[0]=(results[0]+1)%2;
        }
        if(results[14]>0) {
            results[7]=(results[7]+1)%2;
            results[4]=(results[4]+1)%2;
            results[3]=(results[3]+1)%2;
            results[1]=(results[1]+1)%2;
        }
        if(results[13]>0) {
            results[6]=(results[6]+1)%2;
            results[3]=(results[3]+1)%2;
            results[2]=(results[2]+1)%2;
            results[0]=(results[0]+1)%2;
        }
        if(results[12]>0) {
            results[7]=(results[7]+1)%2;
            results[5]=(results[5]+1)%2;
            results[3]=(results[3]+1)%2;
            results[1]=(results[1]+1)%2;
            results[0]=(results[0]+1)%2;
        }
        if(results[11]>0) {
            results[7]=(results[7]+1)%2;
            results[6]=(results[6]+1)%2;
            results[4]=(results[4]+1)%2;
            results[3]=(results[3]+1)%2;
        }
        if(results[10]>0) {
            results[6]=(results[6]+1)%2;
            results[5]=(results[5]+1)%2;
            results[3]=(results[3]+1)%2;
            results[2]=(results[2]+1)%2;
        }
        if(results[9]>0) {
            results[5]=(results[5]+1)%2;
            results[4]=(results[4]+1)%2;
            results[2]=(results[2]+1)%2;
            results[1]=(results[1]+1)%2;
        }
        if(results[8]>0) {
            results[4]=(results[4]+1)%2;
            results[3]=(results[3]+1)%2;
            results[1]=(results[1]+1)%2;
            results[0]=(results[0]+1)%2;
        }
        return (byte) (results[7]*128+results[6]*64+results[5]*32+results[4]*16+results[3]*8+results[2]*4+results[1]*2+results[0]);
    }
    public static byte inverse(byte a) {
        byte result=1;
        for(int i=0;i<254;i++) {
            result=multiply(result, a);
        }
        return result;
    }
    public static byte sbox(byte a) {
        int inverse=inverse(a);
        if(inverse<0) {
            inverse+=256;
        }
        int c[]=new int[8];
        for(int i=0;i<8;i++) {
            c[i]=inverse%2;
            inverse/=2;
        }
        int s[]=new int[8];
        s[7]=(c[7]+c[6]+c[5]+c[4]+c[3])%2;
        s[6]=(c[6]+c[5]+c[4]+c[3]+c[2]+1)%2;
        s[5]=(c[5]+c[4]+c[3]+c[2]+c[1]+1)%2;
        s[4]=(c[4]+c[3]+c[2]+c[1]+c[0])%2;
        s[3]=(c[7]+c[3]+c[2]+c[1]+c[0])%2;
        s[2]=(c[7]+c[6]+c[2]+c[1]+c[0])%2;
        s[1]=(c[7]+c[6]+c[5]+c[1]+c[0]+1)%2;
        s[0]=(c[7]+c[6]+c[5]+c[4]+c[0]+1)%2;
        return (byte) (s[7]*128+s[6]*64+s[5]*32+s[4]*16+s[3]*8+s[2]*4+s[1]*2+s[0]);
    }

    public void substitute() {
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                _states[i][j]=sbox(_states[i][j]);
            }
        }
    }

}
