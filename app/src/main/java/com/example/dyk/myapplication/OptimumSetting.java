package com.example.dyk.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;


/**
 * Created by msi on 2017-03-20.
 */

public class OptimumSetting extends AppCompatActivity
{
    EditText optText;
    RadioGroup onOffRadio;
    int roomNum;
    boolean offRadio;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optimumsetting);
        optText = (EditText)findViewById(R.id.optText);
        Intent intent = getIntent();
       roomNum = intent.getIntExtra("roomNum",0);
        offRadio = false;
        onOffRadio = (RadioGroup)findViewById(R.id.optimumRadioGroup);
        onOffRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(group.getId() == R.id.optimumRadioGroup){
                    switch (checkedId){
                        case R.id.offRadio:
                            offRadio = true;
                    }
                }
            }
        });
    }

    public void Ok(View v) {
        SendOptValue s = new SendOptValue(roomNum,optText.getText().toString(),!offRadio);
        s.sendOptMsg();
        Intent intent = new Intent(OptimumSetting.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}