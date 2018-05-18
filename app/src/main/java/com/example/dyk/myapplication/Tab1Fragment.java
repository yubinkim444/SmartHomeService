package com.example.dyk.myapplication;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.graphics.Typeface;
import com.github.mikephil.charting.data.PieData;
import java.util.ArrayList;

import static android.R.attr.value;
import static com.example.dyk.myapplication.UserInfo.userInfo;

public class Tab1Fragment extends Fragment {
    public static Typeface tf;
    private String[] mLabels = new String[24];

    public Tab1Fragment() {
        for(int i=0; i<24; i++){
            mLabels[i] = Integer.toString(i);
        }
    }

    public static Tab1Fragment newInstance() {
        Tab1Fragment tab =new Tab1Fragment();
        return tab;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        return super.onCreateView(inflater, container, savedInstanceState);
        //return inflater.inflate(R.layout.tab1, container, false);
    }

    protected PieData generatePieData() {
        PieData d;
        int count = 24;
        ArrayList<com.github.mikephil.charting.data.PieEntry> entries1 = new ArrayList<com.github.mikephil.charting.data.PieEntry>(); //<Entry>
        java.util.ArrayList<java.lang.String> xVals = new ArrayList<>(); //<String>

        for (int i = 0; i < 24; i++) {
            xVals.add("Quarter " + Integer.toString(i));
        }
        if(userInfo.resTab[userInfo.clickedTab] != null) {
            if(userInfo.resTab[userInfo.clickedTab].size() == 24) {
                for (int i = 0; i < 24; i++) {
                    xVals.add("entry" + (i + 1));//시간당 하나씩 총 24개
                    float fval = Float.parseFloat(userInfo.resTab[userInfo.clickedTab].get(i).get("predict Value"));
                    String value =  String.format("%.2f",fval);
                    entries1.add(new com.github.mikephil.charting.data.PieEntry((float) 15,value));//15는 15프로씩 차지한다는 이야기, Red BlueOnoff 표시할것
                }

            }
        }
        else{
            for (int i = 0; i < 24; i++) {
                xVals.add("entry" + (i + 1));//시간당 하나씩 총 24개
                entries1.add(new com.github.mikephil.charting.data.PieEntry((float) 15, "no data"));//15는 15프로씩 차지한다는 이야기, Red BlueOnoff 표시할것

            }

        }
        com.github.mikephil.charting.data.PieDataSet ds1 = new com.github.mikephil.charting.data.PieDataSet(entries1, "");
        ds1.setColors(CalculateOnOff(), getContext());//일일이 칼라 어레이 넣어주기...
        ds1.setSliceSpace(2f);

        ds1.setValueTextColor(Color.TRANSPARENT);
        ds1.setValueTextSize(12f);

        d = new PieData(ds1);
        d.setValueTypeface(tf);
        return d;
    }
    int[] CalculateOnOff(){
        int[] onf = new int[24];
        float onoff =0;
        for(int i=0; i<24; i++){
            if(userInfo.resTab[userInfo.clickedTab] != null) {
                if (userInfo.resTab[userInfo.clickedTab].size() == 24) {
                    onoff = Float.parseFloat(userInfo.resTab[userInfo.clickedTab].get(i).get("predict OnOff"));

                }
            }
            if(onoff<0.5){
                onf[i] = R.color.red;
            }
            else{
                onf[i] = R.color.blue;
            }

        }
        return onf;
    }


    private String getLabel(int i) {
        return mLabels[i];
    }



}