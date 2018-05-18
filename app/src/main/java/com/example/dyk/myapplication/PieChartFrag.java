package com.example.dyk.myapplication;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import static com.example.dyk.myapplication.UserInfo.userInfo;

public class PieChartFrag extends Tab1Fragment implements View.OnClickListener {
    ProgressBar progressBar;
    int roomName;
    private AnalysisData mService; //서비스 클래스
    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            AnalysisData.MainServiceBinder binder = (AnalysisData.MainServiceBinder) service;
            mService = binder.getService(); //서비스 받아옴
            mService.registerCallback(mCallback); //콜백 등록
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    //서비스에서 아래의 콜백 함수를 호출하며, 콜백 함수에서는 액티비티에서 처리할 내용 입력
    private AnalysisData.ICallback mCallback = new AnalysisData.ICallback() {
        public void recvData() {

        handler.sendEmptyMessage(0);

        }
    };

    //서비스 시작.
    public void startServiceMethod(View v){
        Intent Service = new Intent(userInfo.context, AnalysisData.class);
        userInfo.context.bindService(Service, mConnection, Context.BIND_AUTO_CREATE);
    }

    public static Tab1Fragment newInstance()
    {
        return new PieChartFrag();
    }
    public PieChartFrag(){

    }

    private PieChart mChart;
    Handler handler;
    View v;
    EditText modify;
    Switch onOff;
    int modiIndex;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab1,container,false);
        roomName = v.getId();
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        mChart = (PieChart) v.findViewById(R.id.pieChart1);
        mChart.setDescription("");
        mChart.setRotationEnabled(false);
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(30f);
         OnChartValueSelectedListener onChartValueSelectedListener = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                modiIndex = (int)h.getX();
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.piechart_modify_popup_menu);
                dialog.setTitle("커스텀 다이얼로그");

                TextView text = (TextView) dialog.findViewById(R.id.dialogText);
                text.setText(Integer.toString(modiIndex)+"시 적정값 수정");
                modify= (EditText)dialog.findViewById(R.id.modifyValue);
                onOff = (Switch)dialog.findViewById(R.id.switchOnOff);
                if(userInfo.resTab[userInfo.clickedTab] == null) {
                    modify.setText("데이터 분석을 먼저 해주세요");
                }
                else{
                    modify.setText(userInfo.resTab[userInfo.clickedTab].get(modiIndex).get("predict Value").substring(0, 4));
                }
                Button cancelBtn = (Button)dialog.findViewById(R.id.calcelBtn);
                Button modifyBtn = (Button)dialog.findViewById(R.id.modifyBtn);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                modifyBtn.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        if(userInfo.resTab[userInfo.clickedTab] != null) {
                            String of = "0";
                            userInfo.resTab[userInfo.clickedTab].get(modiIndex).put("predict Value",modify.getText().toString());
                            if(onOff.isChecked()){
                                of = "1";
                            }
                            userInfo.resTab[userInfo.clickedTab].get(modiIndex).put("predict OnOff",of);
                            mChart.setData(generatePieData());
                            mChart.notifyDataSetChanged();
                            mChart.invalidate();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }

            @Override
            public void onNothingSelected() {

            }
        };
        mChart.setOnChartValueSelectedListener(onChartValueSelectedListener);

        // mChart.setCenterText(generateCenterText());
        // mChart.setCenterTextSize(10f);
        // mChart.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setEnabled(false);

        mChart.setEntryLabelColor(R.color.labelColor);
        mChart.setData(generatePieData());
        Button b = (Button)v.findViewById(R.id.button1);
        Button autoSendService = (Button)v.findViewById(R.id.button2);
        autoSendService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service2 = new Intent(userInfo.context, AutoOptimumSendService.class);
                service2.putExtra("roomNum",userInfo.clickedTab);
                userInfo.context.startService(service2);
            }
        });
        Button cancleService = (Button)v.findViewById(R.id.button3);
        cancleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.autoServiceOff[userInfo.clickedTab] = true;
            }
        });
        b.setOnClickListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                userInfo.context.unbindService(mConnection);
                progressBar.setVisibility(View.INVISIBLE);
                //차트 변경


                mChart.setData(generatePieData());
                mChart.notifyDataSetChanged();
                mChart.invalidate();
               // mChart.setVisibility(View.VISIBLE);

            }
        };



        return v;
    }


    @Override
    public void onClick(View v) {
      //  Intent Service = new Intent(context, AnalysisData.class);
       // context.startService(Service);
       // v.invalidate();
        startServiceMethod(v);
        progressBar.setVisibility(View.VISIBLE);

    }
    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Revenues\nQuarters 2015");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

}




