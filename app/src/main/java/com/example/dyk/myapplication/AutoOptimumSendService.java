package com.example.dyk.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import static com.example.dyk.myapplication.UserInfo.userInfo;


/**
 * Created by msi on 2017-05-05.
 */

public class AutoOptimumSendService extends Service {
    int roomNum;
    SendOptValue sendOptValue;
    public IBinder onBind(Intent intent){
        throw new UnsupportedOperationException("not yet implemented");
    }
    public void onCreate(){

    }
    public void onDestroy(){
        super.onDestroy();
    }
    public int onStartCommand(Intent intent,int flags, int startId){
        roomNum = intent.getIntExtra("roomNum",0);
        if(userInfo.resTab[roomNum] != null) {
            SendThread th = new SendThread();
            th.start();
        }
        return super.onStartCommand(intent,flags,startId);
    }
    class SendThread extends Thread{
        int myTab;
        public SendThread(){
            myTab = userInfo.clickedTab;
        }
        public void run(){
            while(true){
                String msg;
                String hour   = new java.text.SimpleDateFormat("HH").format(new java.util.Date());
                String min   = new java.text.SimpleDateFormat("mm").format(new java.util.Date());
                int h = Integer.parseInt(hour);
                if(userInfo.autoServiceOff[myTab]){
                    break;
                }
                Log.d("Time:::",hour+min);
                try {
                    sleep(1000);
                }
                catch (Exception e){

                }
                if(min.equals("00")) {
                    //Log.d("Time:::",hour+min);
                    boolean on;
                    float onoff = Float.parseFloat(userInfo.resTab[roomNum].get(h).get("predict OnOff"));
                    if (onoff < 0.5) {
                        on = false;
                        msg = "기기제어를 끔";

                    } else {
                        on = true;
                        msg = userInfo.resTab[roomNum].get(h).get("predict OptValue");
                    }
                    sendOptValue = new SendOptValue(roomNum, userInfo.resTab[roomNum].get(h).get("predict OptValue"), on);
                    sendOptValue.sendOptMsg();
                    showNotification(roomNum, msg);
                }
            }

        }
    }
    private void showNotification(int roomNum,String msg){
        Intent activityIntent = new Intent(getApplicationContext(),AutoOptimumSendService.class);
        PendingIntent activityPintent = PendingIntent.getActivity(getApplicationContext(),0,activityIntent,0);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.smile)
                .setContentTitle(userInfo.roomList.get(roomNum).get("roomName")+"의"+userInfo.roomList.get(roomNum).get("electroType")+"적정값 설정")
                .setContentText("정적값을 "+msg+" (으)로 설정하였습니다.")
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(activityPintent);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationBuilder.build());
    }
}