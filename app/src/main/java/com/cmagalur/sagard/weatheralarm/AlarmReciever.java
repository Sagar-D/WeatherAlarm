package com.cmagalur.sagard.weatheralarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by sagar on 14-05-2017.
 */

public class AlarmReciever extends BroadcastReceiver {

    JSONObject weatherObject;
    GPSTracker gps ;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("abcd","IM INSIDE ALARMRECIEVER");
        gps = new GPSTracker(context);

        Log.d("Sagar","created gps object");
        if(intent.getExtras().getString("operation").equals("start")) {
            String city="",temperature="",humidity="",pressure="";
            getWeather();
            try {
                city = weatherObject.getString("name");
                JSONObject subObject = weatherObject.getJSONObject("main");
                temperature = subObject.getString("temp");
                pressure = subObject.getString("pressure");
                humidity = subObject.getString("humidity");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent imusic = new Intent(context, RingtoneService.class);
            imusic.putExtra("operation", "start");
            imusic.putExtra("city",city);
            imusic.putExtra("temperature",temperature);
            imusic.putExtra("pressure",pressure);
            imusic.putExtra("humidity",humidity);
            context.startService(imusic);

            Log.d("abcd",city+temperature+humidity+pressure);
        }
        else{
            Intent imusic = new Intent(context, RingtoneService.class);
            imusic.putExtra("operation", "stop");
            context.startService(imusic);        }
    }

    private void getWeather(){

        class Weather extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {


                JSONObject ob=null;
                String result="",temp;


                try {


                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+gps.LATITUDE+"&lon="+gps.LONGITUDE+"&appid=3c20372906a2efd8164ae6416f1dd3cc");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.connect();

                    InputStream inStream = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

                    while((temp=reader.readLine()) != null){

                        result += temp;

                    }

                    ob=new JSONObject(result);

                }catch (Exception e){
                    e.printStackTrace();
                }


                return result;
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);

            }
        }

        Weather w = new Weather();
        w.execute();
        String str=null;
        try {
            str = w.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            weatherObject = new JSONObject(str);
        }catch(Exception e){
            e.printStackTrace();
        }


    }

}
