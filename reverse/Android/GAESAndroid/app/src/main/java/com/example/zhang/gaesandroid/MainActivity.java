package com.example.zhang.gaesandroid;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhang.gaes.LightState;



public class MainActivity extends AppCompatActivity {


    public static String encrypt(String plainText, String key) throws Exception {
        byte[] keyBytes=key.getBytes("UTF-8");
        byte[] plainTextBytes=plainText.getBytes("UTF-8");
        int length=(plainTextBytes.length+15)/16*16;
        byte[] resultBytes=new byte[length];
        for(int i=0;i<plainTextBytes.length;i+=16) {
            byte inputBytes[]=new byte[16];

            for(int j=0;j<16;j++) {
                if(i+j<plainTextBytes.length) {
                    inputBytes[j]=plainTextBytes[i+j];
                } else {
                    break;
                }

            }
            LightState state=new LightState(inputBytes);
            for(int round=0;round<=10;round++) {
                byte roundKey[]=new byte[16];
                for(int j=0;j<16;j++) {
                    roundKey[j]=keyBytes[round*16+j];
                }
                if(round==0) {
                    state.addRoundKey(roundKey);
                } else if(round<10) {
                    state.substitute();
                    state.shiftRows();
                    state.mixCloumns();
                    state.addRoundKey(roundKey);
                } else {
                    state.substitute();
                    state.shiftRows();
                    state.addRoundKey(roundKey);
                }
            }
            for(int row=0;row<4;row++) {
                for(int column=0;column<4;column++) {
                    resultBytes[i+4*row+column]=state.value(row, column);
                }
            }
        }

        return Base64.encodeToString(resultBytes, Base64.NO_WRAP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText plainTextView=this.findViewById(R.id.plainTextView);
        Button verifyButton=this.findViewById(R.id.verifyButton);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String key="This is a AES-like encryption algorithm. "
                            + "However, we do some change. "
                            + "Therefore, you cannot directly use security class to decrypt the message. "
                            + "Our challenge is to find the plain text of this encrypt message with th fixed key. ";
                    String plainText=plainTextView.getText().toString();
                    String encryptedText="eaZwtl5nsHW3ledvoZCdFla5yG13p2Txfq3AN7LEX7s2uK+v7x2Wsz/7jbe0G6R2";
                    String message=null;
                    if(encrypt(plainText, key).equals(encryptedText)) {
                        message="Correct plainText";

                    } else {
                        message="Incorrect plainText";
                    }
                    new AlertDialog.Builder(MainActivity.this).setMessage(message).setPositiveButton("OK", null).show();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
