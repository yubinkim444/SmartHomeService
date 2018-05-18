package com.example.dyk.myapplication;


/**
 * Created by hansangjun on 2017. 2. 24..
 */
public class ListViewItem {

    private String roomName;
    private String roomIp;
    private String roomData;
    private String electroType ;
    private String dateStr;

    public void setRoomName(String title) {
        roomName= title ;
    }
    public void setRoomIp(String ip){roomIp = ip;}
    public void setRoomData(String value){roomData = value;}
    public void setElectroType(String type){electroType=type;}
    public void setDateStr(String date){dateStr=date;}


    public String getRoomName() {
        return this.roomName ;
    }
    public String getElectroType() { return this.electroType ; }
    public String getDateStr() { return this.dateStr ; }
    public String getRoomIp(){return this.roomIp;}
    public String getRoomData(){return this.roomData;}



}