package com.example.dyk.myapplication;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.type;
import static com.example.dyk.myapplication.R.id.roomIp;
import static com.example.dyk.myapplication.R.id.roomName;
import static com.example.dyk.myapplication.UserInfo.userInfo;

/**
 * Created by msi on 2017-06-07.
 */

public class ModiRoom extends Thread{
        String roomId;
        String roomName;
        String roomIp;
        String type;
        String myResult;
        boolean modiSuccess = false;
        ModiRoom(int pos, String name,String ip,String type){

            roomId = userInfo.roomList.get(pos).get("roomId");
            roomIp = ip;
            roomName = name;
            this.type = type;
        }
        public void run(){
            try {
                //--------------------------
                //   URL 설정하고 접속하기
                //--------------------------
                URL url = new URL("http://220.76.187.135/modiRoom.php");       // URL 설정
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

                buffer.append("roomId").append("=").append(roomId).append("&");
                buffer.append("roomName").append("=").append(roomName).append("&");
                buffer.append("roomIp").append("=").append(roomIp).append("&"); // php 변수에 값 대입
                buffer.append("electroType").append("=").append(type);

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
                    if (!myResult.equals("")) {
                        modiSuccess = true;

                        Log.d("modi Success", myResult);
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


