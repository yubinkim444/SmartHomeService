package com.example.dyk.myapplication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import static com.example.dyk.myapplication.UserInfo.userInfo;

/**
 * Created by msi on 2017-05-08.
 */

public class SendOptValue {
    String roomIp;
    String elecType;
    String optMsg;
    String offMSg;
    String optValue;
    Thread Thread;
    Socket socket;
    int port = 5100;
    boolean on;
    String ip = userInfo.myInfo.get(0).get("managerIp");
    SendOptValue(int roomNum, String optValue,boolean onoff){
        on = onoff;
        roomIp = userInfo.roomList.get(roomNum).get("roomIp");
        this.optValue = optValue;
        elecType = userInfo.roomList.get(roomNum).get("electroType");
        readyMsg();
    }
    public void readyMsg(){

        int optData =0 ;
        if(!on){
            optValue = "F";
        }
        else {
            optData = Integer.parseInt(optValue) + 100;
        }
        switch (elecType){
            case "냉방기":
                optMsg = "UT" + roomIp.length() + roomIp + optValue + '\0';
                break;
            case "난방기":
                optMsg = "UT" + roomIp.length() + roomIp + Integer.toString(optData) + '\0';
                break;
            case "가습기":
                optMsg = "UH" + roomIp.length() + roomIp + optValue + '\0';
                break;
            case "제습기":
                optMsg = "UH" + roomIp.length() + roomIp + Integer.toString(optData)+ '\0';
                break;
            case "조명":
                optMsg = "UL" + roomIp.length() + roomIp + optValue + '\0';
                break;
            case "움직임 감지":
                break;
            default: break;

        }
    }

    public void sendOptMsg() {
        Thread = new Thread() {
            public void run(){
                super.run();
                try {
                    socket = new Socket(ip, port);
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.write(optMsg);
                    out.flush();
                    socket.close();
                }
                catch(IOException e) {
                    System.out.println(e.getMessage());
                }
                finally {
                }
            }
        };
        Thread.start();
    }
}
