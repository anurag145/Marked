package com.github.anurag145.impulseattendance.activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.anurag145.impulseattendance.schedulingServices.Alarm;
import com.github.anurag145.impulseattendance.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Alarm ob = new Alarm();
       ob.setAlarm(getApplicationContext());



    }


}
