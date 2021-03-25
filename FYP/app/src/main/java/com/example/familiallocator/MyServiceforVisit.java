package com.example.familiallocator;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyServiceforVisit extends Service {

    private DatabaseReference reference;
    private final Handler handler = new Handler();
    private Runnable runnable;
    SharedPreferences pref;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        get();

        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 60000);
                get();
            }
        }, 60000);

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent rsi = new Intent(getApplicationContext(), this.getClass());
        rsi.setPackage(getPackageName());
        startService(rsi);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void get() {


        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        reference = FirebaseDatabase.getInstance().getReference();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        editor.putString("v_lat", String.valueOf(location.getLatitude()));
                        editor.putString("v_lang", String.valueOf(location.getLongitude()));
                        editor.apply();

                        storeinfirebase();

                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

    }

    private void storeinfirebase() {

        final String id = pref.getString("id","null");
        final String lat = pref.getString("v_lat","31.5204");
        final String lang = pref.getString("v_lang","74.3587");

        if (id.equals("null")){
           //Do Nothing
        }
        else {

            String area = null;
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
            try {
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat)
                        , Double.parseDouble(lang)
                        , 1);

                if(addresses.size()>0) {
                    area =addresses.get(0).getAddressLine(0);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("lat", lat);
            map.put("lang", lang);
            map.put("area", area);

            reference.child("Visit_Points").child(id).child(lat.replace(".","").concat(lang.replace(".",""))).setValue(map);

        }
    }

}