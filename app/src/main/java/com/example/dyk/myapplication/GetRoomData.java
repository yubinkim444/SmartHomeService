package com.example.dyk.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.dyk.myapplication.UserInfo.userInfo;

/**
 * Created by msi on 2017-05-31.
 */

class GetRoomData extends Thread{

    String myResult;
    public void run(){
        try {
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://220.76.187.135/getAllRooms.php");       // URL 설정
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

            buffer.append("memberCode").append("=").append(userInfo.myInfo.get(0).get("memberCode"));
            Log.d("mycode",userInfo.myInfo.get(0).get("memberCode"));
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
            synchronized (this) {
                myResult = builder.toString();
                Log.d("Result", myResult);
            }
            userInfo.roomList = new ArrayList<HashMap<String, String>>();
            JSONArray arrResults = new JSONArray(myResult);
            int iCount = arrResults.length();

            String [] jsonName = {"roomId","roomName","electroType","roomInsertDate","roomIp"};
            for( int i = 0; i < iCount; i++ ) {
                JSONObject obj =  arrResults.getJSONObject(i);
                HashMap<String, String> h = new HashMap<>();
                for (int j = 0; j < jsonName.length; j++) {
                    h.put(jsonName[j], obj.getString(jsonName[j]));
                    Log.d(jsonName[j],obj.getString(jsonName[j]));
                }
                userInfo.roomList.add(h);
            }
        } catch (Exception e) {

        }
    }
}
