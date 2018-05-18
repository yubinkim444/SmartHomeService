package com.example.dyk.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import static com.example.dyk.myapplication.UserInfo.userInfo;


/**
 * Created by msi on 2017-03-05.
 */

public class AddRoomActivity extends AppCompatActivity {
    EditText roomName;
    EditText roomIp;
    String type;
    Spinner typeSpinner;
    String myResult;
    boolean addSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room_activity);
        addSuccess = false;
        typeSpinner = (Spinner) findViewById(R.id.spinner);
// adapter를 정의하고 items을 dropdown 형태로 붙인다.
        ArrayAdapter<CharSequence> aa= ArrayAdapter.createFromResource(this,R.array.items ,android.R.layout.simple_spinner_dropdown_item);

// spin에  adapter를 붙여넣는다.
        typeSpinner.setAdapter(aa);
        roomName = (EditText)findViewById(R.id.roomName);
        roomIp = (EditText)findViewById(R.id.roomIp);

    }
    public void preButton(View v){
        Intent intent = new Intent(AddRoomActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void onClickRegisterButton(View v){
        int pos = typeSpinner.getSelectedItemPosition();//pos는 0부터 시작임
        type = ((TextView)typeSpinner.getSelectedView()).getText().toString();
        AddRoom addRoom = new AddRoom();
        addRoom.start();
        try {
            addRoom.join();
            if(addSuccess){
                Toast.makeText(this, "등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddRoomActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(this,  "서버연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }

    }
    class AddRoom extends Thread{
        public void run(){
            try {
                //--------------------------
                //   URL 설정하고 접속하기
                //--------------------------
                URL url = new URL("http://220.76.187.135/addRoom.php");       // URL 설정
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
                buffer.append("memberCode").append("=").append(userInfo.myInfo.get(0).get("memberCode")).append("&");
                buffer.append("roomName").append("=").append(roomName.getText().toString()).append("&");
                buffer.append("roomIp").append("=").append(roomIp.getText().toString()).append("&"); // php 변수에 값 대입
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
                        addSuccess = true;
                        Log.d("add Success", myResult);
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