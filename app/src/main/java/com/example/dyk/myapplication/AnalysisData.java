package com.example.dyk.myapplication;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


import static com.example.dyk.myapplication.UserInfo.userInfo;

/**
 * Created by msi on 2017-05-05.
 */

public class AnalysisData extends Service {
    String myResult;
    String ipz = userInfo.roomList.get(userInfo.clickedTab).get("roomIp");
    String managerCodes = userInfo.myInfo.get(0).get("managerCode");

    String sendUrl;
    String sendApiKey;

    private final IBinder mBinder = new MainServiceBinder();
    //서비스 바인더 내부 클래스 선언
    public class MainServiceBinder extends Binder {
        AnalysisData getService() {
            return AnalysisData.this; //현재 서비스를 반환.
        }
    }
    //콜백 인터페이스 선언
    public interface ICallback {
        public void recvData(); //액티비티에서 선언한 콜백 함수.
    }

    private ICallback mCallback;

    //액티비티에서 콜백 함수를 등록하기 위함.
    public void registerCallback(ICallback cb) {
        mCallback = cb;
    }

    //액티비티에서 서비스 함수를 호출하기 위한 함수 생성
    public void myServiceFunc(){
        //서비스에서 처리할 내용
        SendThread st = new SendThread(managerCodes,ipz,"2017-04-21");
        st.start();
    }

    public IBinder onBind(Intent intent){
        initiaiton();
        SimpleDateFormat frm =new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String today = frm.format(cal.getTime());
        SendThread st = new SendThread(managerCodes,ipz,today);
        st.start();
        return mBinder;
    }
    public void initiaiton(){
        switch (userInfo.roomList.get(userInfo.clickedTab).get("electroType")){
            case "냉방기":
            case "난방기":
               sendUrl = userInfo.tempUrl;
                sendApiKey = userInfo.tempApikey;
                Log.d("Temp","datatest");
                break;
            case "가습기":
            case "제습기":
                sendUrl = userInfo.humdUrl;
                sendApiKey = userInfo.humdApiKey;
                Log.d("Humd","datatest");
                break;
            case "조명":
                sendUrl = userInfo.luxUrl;
                sendApiKey = userInfo.luxApikey;
                Log.d("Lux","datatest");
                break;
            case "움직임 감지":
                sendUrl = userInfo.tempUrl;
                sendApiKey = userInfo.tempApikey;
                break;
            default: break;

        }
    }
    public void onCreate(){
        userInfo.resTab[userInfo.clickedTab] = new ArrayList<>();
    }
    public void onDestroy(){
        super.onDestroy();
    }
    public int onStartCommand(Intent intent,int flags, int startId){

        return super.onStartCommand(intent,flags,startId);
    }

    class SendThread extends Thread{
        String managerCode;
        String ip;
        String date;
        SendThread(String mc,String ips, String dt){
            this.managerCode = mc;
            this.ip = ips;
            this.date = dt;
        }
        public void run() {
            for (int i = 0; i < 24; i++) {
                try {
                    sleep(800);
                }
                catch (Exception e){

                }
                String time = Integer.toString(i);
                String zeroTime;
                String tmAndDate;
                if(i < 10 ){zeroTime = "0"+time;}
                else{zeroTime = time;}
                tmAndDate = date+"T"+zeroTime+":00:00Z";

                try {

                    //--------------------------
                    //   URL 설정하고 접속하기
                    //--------------------------
                    //progressBar.incrementProgressBy(100/24);
                    Log.d("url", "start");
                    URL url = new URL(sendUrl);       // URL 설정
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
                    //--------------------------
                    //   전송 모드 설정 - 기본적인 설정이다
                    //--------------------------
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);                         // 서버에서 읽기 모드 지정
                    http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
                    // 전송 방식은 POST

                    // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    http.setRequestProperty("Accept-Charset", "UTF-8");
                    http.setRequestProperty("Accept", "application/json");
                    String basicAuth = "Bearer " + sendApiKey;
                    http.setRequestProperty("Authorization", basicAuth);
                    http.setRequestMethod("POST");
                    //--------------------------
                    //   서버로 값 전송
                    //--------------------------
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("{\n" +
                            "        \"Inputs\": {\n" +
                            "                \"input1\":\n" +
                            "                [\n" +
                            "                    {\n" +
                            "                            'managerCode': \"" + managerCode + "\",   \n" +
                            "                            'ip': \"" + ip + "\",   \n" +
                            "                            'time': \"" + time + "\",   \n" +
                            "                            'date': \"" + tmAndDate + "\",   \n" +
                            "                    }\n" +
                            "                ],\n" +
                            "        },\n" +
                            "    \"GlobalParameters\":  {\n" +
                            "    }\n" +
                            "}\n");
                    OutputStream outStream = http.getOutputStream();
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();
                    //--------------------------
                    //   서버에서 전송받기
                    //--------------------------
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                        builder.append(str);
                    }
                    myResult = builder.toString();
                    Log.d("Result", myResult);

                    if (myResult.equals(null)) {
                        Log.d("no result error occur", "");
                    } else {
                    }

                    JSONObject obj = new JSONObject(myResult).getJSONObject("Results");
                    JSONArray arr1 = obj.getJSONArray("output1");
                    JSONObject data = arr1.getJSONObject(0);
                    String[] jsonName = {"predict OptValue", "predict Value", "predict OnOff"};
                    HashMap<String, String> h = new HashMap<>();
                    for (int j = 0; j < jsonName.length; j++) {
                        h.put(jsonName[j], data.getString(jsonName[j]));
                        Log.d(time, "!");
                    }
                    userInfo.resTab[userInfo.clickedTab].add(h);
                } catch (Exception e) {
                    break;
                }
            }
            mCallback.recvData();

        }
    }

}

