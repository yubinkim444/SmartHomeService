package com.example.dyk.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.entries;
import static com.example.dyk.myapplication.R.id.lineChart;
import static com.example.dyk.myapplication.R.id.progressBar;
import static com.example.dyk.myapplication.R.id.roomData;
import static com.example.dyk.myapplication.Tab1Fragment.tf;
import static com.example.dyk.myapplication.UserInfo.userInfo;

/**
 * Created by msi on 2017-07-09.
 */

public class LineFrag extends Fragment{

    LineDataSet dataSet ;
    private LineChart mChart;
    int roomName;
    public static LineFrag newInstance()
    {
        return new LineFrag();
    }
    public LineFrag(){}
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

       View v = inflater.inflate(R.layout.time_fragment,container,false);
        roomName = v.getId();
        mChart = (LineChart) v.findViewById(lineChart);
        // mChart.getDescription("").setEnabled(false);

        mChart.setDrawGridBackground(false);
        mChart.setData(generateLineData());
        mChart.notifyDataSetChanged();
        mChart.invalidate();
        mChart.animateX(3000);
        mChart.getAxisLeft().setAxisMaxValue(60f);
        mChart.getAxisLeft().setAxisMinValue(0f);
        Legend l = mChart.getLegend();
        YAxis leftAxis = mChart.getAxisLeft();

        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
        return v;
    }

    LineData generateLineData(){
        List<Entry> entries = new ArrayList<Entry>();
        if(!userInfo.roomList.get(userInfo.clickedTab).get("electroType").equals("움직임 감지")) {
            Log.d("NOOOOOOmosionGraph!!",userInfo.roomList.get(userInfo.clickedTab).get("electroType"));
            for (int j = 0; j < userInfo.roomData.get(userInfo.clickedTab).size(); j++) {
                // String date = userInfo.roomData.get(userInfo.clickedTab).get(j).get("date");
                try {
                    //  Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    entries.add(new Entry(j + 1, Float.parseFloat(userInfo.roomData.get(userInfo.clickedTab).get(j).get("value"))));
                } catch (Exception e) {
                }
            }
        }
        else{
            Log.d("motionSensor!!!!!!!","Crate motion Graph");
               ArrayList<Integer> motion = generateMotionGraph();
            for(int j=0 ;j<motion.size(); j++) {
                try {
                    //  Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    entries.add(new Entry(j, motion.get(j)));
                } catch (Exception e) {
                }
            }

        }
        dataSet= new LineDataSet(entries,"label");
        dataSet.setColor(R.color.blue);
        dataSet.setValueTextColor(R.color.red);
        LineData lineData = new LineData(dataSet);
        lineData.setValueTypeface(tf);
        return lineData;

    }
    //motionData
    ArrayList<Integer> generateMotionGraph(){
        Log.d("mosionGraph!!","motion!!!!!!!!!!!!");
        ArrayList<Integer> result = new ArrayList<>();
        String prTime = userInfo.roomData.get(userInfo.clickedTab).get(0).get("sensingTime").substring(0,10);
        int index=0;
        for (int j = 1; j < userInfo.roomData.get(userInfo.clickedTab).size(); j++) {
            String nxTime = userInfo.roomData.get(userInfo.clickedTab).get(j).get("sensingTime").substring(0,10);
            Log.d("nextTime",nxTime);
           if( prTime.equals(nxTime)){
               index++;
           }
           else{
               result.add(index);
               index=0;
           }
            prTime = nxTime;
        }
        result.add(index);
        Log.d("motion",result.toString());
        return result;
    }
}
