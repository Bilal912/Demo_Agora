package com.edu_touch.edu_hunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import es.dmoral.toasty.Toasty;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Splash extends AppCompatActivity {
    Thread t;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    String city;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        t = new Thread() {
            public void run() {
                try {
                    t.sleep(2500);

                    if (ContextCompat.checkSelfPermission(Splash.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        gonext();
                    } else {
                        checkLocationPermission();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }

    public void gonext() {

        String Email = sharedPreferences.getString("email", "Null");
        if (Email.equals("Null")) {

            getLocation();

            Intent i = new Intent(Splash.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else{
            getLocation();

            checkuser(sharedPreferences.getString("id", "Null"));
    }
}

    private void checkuser(String string) {
        Map<String, String> params = new Hashtable<String, String>();
        params.put("std_id",string);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_checkinguser, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String code = response.getString("error code");

                    if (code.equals("200")){
                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);

                            if (object.getString("login_status").equals("0")){
                                SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
                                preferences.edit().clear().commit();

                                Intent intent = new Intent(Splash.this,Warning.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                Intent i = new Intent(Splash.this, Home.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }
                    else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                animationView.setVisibility(View.GONE);
                Toasty.error(Splash.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
            }
        });
        jsonRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        RequestQueue queue = Volley.newRequestQueue(Splash.this);
        queue.add(jsonRequest);
    }

    public void getLocation(){

        SharedPreferences.Editor leditor=sharedPreferences.edit();

        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Splash.this);

        if(ActivityCompat.checkSelfPermission(Splash.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Splash.this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
            },2);

        }else {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener( new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {

                                leditor.putString("lat", String.valueOf(location.getLatitude()));
                                leditor.putString("lang", String.valueOf(location.getLongitude()));

                                Geocoder geocoder = new Geocoder(Splash.this, Locale.ENGLISH);
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude()
                                            , location.getLongitude()
                                            , 1);


                                    if(addresses.size()>0) {

                                        city = addresses.get(0).getSubAdminArea();
                                        if(city == null) {
                                        }
                                        else {
                                            leditor.putString("city", city);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                leditor.apply();

                                location.reset();
                            }
                            else{
                                
//                                Toast.makeText(Splash.this, "No Data Found"
//                                        , Toast.LENGTH_SHORT).show();
                            }

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Location Permission Is Necessary")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Splash.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        gonext();

                        //Request location updates:
                        //locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    checkLocationPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }

        }
    }


}