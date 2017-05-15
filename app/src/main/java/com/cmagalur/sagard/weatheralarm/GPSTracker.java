package com.cmagalur.sagard.weatheralarm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sagar on 14-05-2017.
 */

public class GPSTracker implements LocationListener {

    Context context;

    LocationManager locationManager;
    LocationListener locationListener;
    Location location;
    public double LATITUDE = 12.9762, LONGITUDE = 77.6033;
    String err = "";
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 0;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

    GPSTracker(Context c) {
        context = c;
        initialize();
    }

    private void initialize() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("Sagar", "GPS...GPS");

             location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Log.d("Sagar", "Im in");
                Log.d("Sagar", "LATTITUDE : " + location.getLatitude());
                Log.d("Sagar", "Longitude : " + location.getLongitude());
                LATITUDE = location.getLatitude();
                LONGITUDE = location.getLongitude();
            } else {
                Log.d("Sagar", "GPS ... no last known location :(");
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null) {
                LATITUDE = location.getLatitude();
                LONGITUDE = location.getLongitude();
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
        Log.d("Sagar","AGAIN...IM in");

        LATITUDE = location.getLatitude();
        LONGITUDE = location.getLongitude();

        Toast.makeText(
                context,
                "Location changed: Lat: " + location.getLatitude() + " Lng: "
                        + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }



}
