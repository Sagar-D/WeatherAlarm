package com.cmagalur.sagard.weatheralarm;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker ;
    Button bt_set, bt_reset;
    TextView txt;

    static String initText = "";

    Intent i;
    PendingIntent pendingIntent;

    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("abcd","HALLOOO");


        int MY_PERMISSION_ACCESS_FINE_LOCATION=0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }




        timePicker = (TimePicker)findViewById(R.id.timePicker);
        txt = (TextView)findViewById(R.id.textView);

        txt.setText(initText);

        bt_set = (Button)findViewById(R.id.bt_set);
        bt_reset = (Button)findViewById(R.id.bt_reset);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        bt_set.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                i = new Intent(MainActivity.this,AlarmReciever.class);
                i.putExtra("operation","start");
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this,1,i,PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
                calendar.set(Calendar.SECOND,0);
                //calendar.add(Calendar.SECOND,10);
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                txt.setText("ALARM SET AT "+timePicker.getHour()+":"+timePicker.getMinute());
            }
        });


        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt.setText("");
                initText="";

                alarmManager.cancel(pendingIntent);
                i = new Intent(MainActivity.this,AlarmReciever.class);
                i.putExtra("operation","stop");
                sendBroadcast(i);

            }
        });

    }
}
