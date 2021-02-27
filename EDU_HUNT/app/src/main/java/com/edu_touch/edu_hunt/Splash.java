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

import java.util.Hashtable;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Splash extends AppCompatActivity {
    Thread t;
    private AlertDialog dialog;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);

        t = new Thread() {
            public void run() {
                try {
                    t.sleep(2500);

                    if (ContextCompat.checkSelfPermission(Splash.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Splash.this,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Splash.this,
                                    android.Manifest.permission.INTERNET)
                                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(Splash.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Splash.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                    ) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                android.Manifest.permission.INTERNET)
                                && ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)&& ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                && ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                android.Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                        ) {
                            //gonext();
                        } else {
                            ActivityCompat.requestPermissions(Splash.this,
                                    new String[]{
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            android.Manifest.permission.INTERNET,
                                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                                            android.Manifest.permission.ACCESS_NETWORK_STATE,
                                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    },
                                    102);
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };t.start();

    }

    public void gonext(){

        getLocation();

        String Email = sharedPreferences.getString("email","Null");
        if (Email.equals("Null")) {
            Intent i = new Intent(Splash.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            checkuser(sharedPreferences.getString("id","Null"));
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
//                        textView.setVisibility(View.VISIBLE);
//                        animationView.setVisibility(View.GONE);
//                        Toasty.error(My_teacher.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {

//                    textView.setVisibility(View.VISIBLE);
//                    animationView.setVisibility(View.GONE);
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
        LocationManager locationManager;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Splash.this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                        public void onSuccess(android.location.Location location) {

                            if (location != null) {

                                leditor.putString("lat", String.valueOf(location.getLatitude()));
                                leditor.putString("long", String.valueOf(location.getLongitude()));
                                leditor.apply();

                                location.reset();
                            }
                            else{
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 102) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
            ) {
                //gonext();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                builder.setMessage("App required some permission please enable it")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openPermissionScreen();
                            }
                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                dialog = builder.show();
            }

            return;
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        t = new Thread() {
            public void run() {
                try {
                    t.sleep(2500);

                    if (ContextCompat.checkSelfPermission(Splash.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        gonext();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };t.start();

    }

    public void openPermissionScreen() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", Splash.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}