package com.cmagalur.sagard.weatheralarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by sagar on 14-05-2017.
 */

public class RingtoneService extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("abcd","IM INSIDE RINGTONESERVICE");

        if(intent.getExtras().getString("operation").equals("start")) {

            MainActivity.initText = "Weather Report : \n"+"Temperature : "+intent.getExtras().getString("temperature")+"K";
            MainActivity.initText += "\nPressure : "+intent.getExtras().getString("pressure");
            MainActivity.initText += "\nHumidity : "+intent.getExtras().getString("humidity")+"%";

            if(Double.parseDouble(intent.getExtras().getString("temperature"))>280) {

                Intent notificationIntent = new Intent(this, MainActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

                Notification notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notify_img)
                        .setContentTitle("ALARM")
                        .setContentText("Its Time to WakeUP...")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent).build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(1, notification);

                mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }else{

                Intent notificationIntent = new Intent(this, MainActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

                Notification notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notify_img)
                        .setContentTitle("ALARM")
                        .setContentText("Missed Alarm..bcaz of BAD WEATHER")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent).build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(1, notification);

            }

        }
        else
            mediaPlayer.stop();

        return START_NOT_STICKY;
    }


}
