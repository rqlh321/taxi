package com.example.taxidriverapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.taxidriverapp.models.Area;

public class GpsService extends Service {
    public final static String MY_ACTION = "MY_ACTION";

    public int onStartCommand(Intent intent, int flags, int startId) {
        gpsMonitoring();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void gpsMonitoring() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() < 1000.0) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    double speed = location.getSpeed();

                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    intent.putExtra("speed", speed);
                    sendBroadcast(intent);

                    for (Area area : TaxiServer.Data.areas) {
                        if (area.getLat() > lat && (area.getLat() - 0.060145) < lat &&
                                area.getLon() < lon && (area.getLon() + 0.034447) > lon) {
                            if (TaxiServer.Data.myArea != area.getId()) {
                                TaxiServer.Data.myArea = area.getId();
                                TaxiServer.getInstance().setMyArea(area.getId());
                                Toast.makeText(GpsService.this, getString(R.string.new_area) + area.getName(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(), true), 0, 0, locationListener);
    }

}
