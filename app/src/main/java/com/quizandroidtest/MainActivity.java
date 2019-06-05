package com.quizandroidtest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.quizandroidtest.services.AlarmService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private final int UNIQUE_ID = 120192;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
    private TextView tv_prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_prod = findViewById(R.id.tv_prod);
        tv_prod.setText(BuildConfig.center_name);

        pref = getSharedPreferences("Quiz", 0); // 0 - for private mode
        editor = pref.edit();

        // Intialize AlarmManager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        // Make call the AlarmService with Pending Intent
        Intent myIntent = new Intent(getApplicationContext(), AlarmService.class);
        myIntent.putExtra("msg", "You are on the break");
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), UNIQUE_ID, myIntent, 0);

        setAlarm();

        // check if app is open first time
        if (pref.getLong("intervalTime", 0) == 0) {
            int intervalTime = 5 * 60 * 1000;
            editor.putLong("intervalTime", intervalTime); // Storing string
            editor.commit();

        }


    }

    // Set Alarm for enable timer
    private void setAlarm() {
        long _alarm = 0;
        Calendar alarm = Calendar.getInstance();
        _alarm = alarm.getTimeInMillis();

        {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, _alarm, pendingIntent);
            updateTime(simpleDateFormat.format(alarm.getTime().getTime()));
        }
    }

    private void cancelAlarm() {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void updateTime(String aTime) {

        editor.putString("time", aTime); // Storing string
        editor.commit();
        Log.e("DEEP", "Time :" + aTime);

    }

    // Alarm fire for enable timer
    @Override
    protected void onPause() {
        setAlarm();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        setAlarm();
        super.onDestroy();
    }
}
