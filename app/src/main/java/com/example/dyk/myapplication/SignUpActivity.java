package com.example.dyk.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DYK on 2017-01-20.
 */

public class SignUpActivity extends AppCompatActivity {
    String myResult;
    EditText id;
    EditText managerIp;
    EditText pw;
    EditText name;
    EditText address;
    boolean loginSuccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        id = (EditText)findViewById(R.id.id);
        managerIp = (EditText)findViewById(R.id.homeIP);
        pw = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        address = (EditText)findViewById(R.id.address);
        loginSuccess = false;

    }


    public void preButton(View v) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    public void signUpButton(View v) {
        connectServer();

    }

    public void connectServer() {

        Connect c = new Connect();
        c.start();
        try {
            c.join();
            if (loginSuccess) {
                Toast.makeText(this, "Signup Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Signup fail check your info.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (InterruptedException e){
            Toast.makeText(this, "Server Connect Error", Toast.LENGTH_SHORT).show();
        }
    }

    class Connect extends Thread{
        public void run(){
            try {
                //--------------------------
                //   URL 설정하고 접속하기
                //--------------------------
                URL url = new URL("http://220.76.187.135/SignUp.php");       // URL 설정
                HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
                //--------------------------
                //   전송 모드 설정 - 기본적인 설정이다
                //--------------------------
                http.setDefaultUseCaches(false);
                http.setDoInput(true);                         // 서버에서 읽기 모드 지정
                http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
                http.setRequestMethod("POST");         // 전송 방식은 POST

                // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                //--------------------------
                //   서버로 값 전송
                //--------------------------
                StringBuffer buffer = new StringBuffer();
                buffer.append("name").append("=").append(name.getText().toString()).append("&");
                buffer.append("id").append("=").append(id.getText().toString()).append("&");
                buffer.append("ip").append("=").append(managerIp.getText().toString()).append("&");
                buffer.append("address").append("=").append(address.getText().toString()).append("&");                 // php 변수에 값 대입
                buffer.append("pw").append("=").append(pw.getText().toString());

                OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(),"utf8");
                PrintWriter writer = new PrintWriter(outStream);
                writer.write(buffer.toString());
                writer.flush();
                //--------------------------
                //   서버에서 전송받기
                //--------------------------
                InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                    builder.append(str);                     // View에 표시하기 위해 라인 구분자 추가
                }
                myResult = builder.toString();
                // 전송결과를 전역 변수에 저장
                synchronized (this) {
                    myResult = builder.toString();
                    // 전송결과를 전역 변수에 저장
                    if (myResult.equals("success")) {
                        loginSuccess = true;
                        Log.d("login Success", myResult);
                    }
                    Log.d("Result", myResult);
                }
                Log.d("Result",myResult);
            } catch (MalformedURLException e) {
                //
            } catch (IOException e) {
                //
            } // try
        } // HttpPostData


    } // HttpPostData


}




