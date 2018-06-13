package com.github.anurag145.impulseattendance;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;




public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Alarm ob = new Alarm();
       ob.setAlarm(getApplicationContext());



    }


}
