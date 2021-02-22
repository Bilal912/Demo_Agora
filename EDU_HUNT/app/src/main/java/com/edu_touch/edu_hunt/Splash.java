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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Splash extends AppCompatActivity {
Thread t;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2500;

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
                                    != PackageManager.PERMISSION_GRANTED
                    ) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                android.Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                android.Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                        ) {
                            gonext();
                        } else {
                            ActivityCompat.requestPermissions(Splash.this,
                                    new String[]{
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
        } else {
            Intent i = new Intent(Splash.this, Home.class);
            startActivity(i);
        }
        finish();
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

//                                Toast.makeText(Splash.this, location.getLatitude()+" "+location.getLongitude()
//                                        , Toast.LENGTH_SHORT).show();

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
                gonext();
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
////        handler.postDelayed( runnable = new Runnable() {
////            public void run() {
////                //do something
////                handler.postDelayed(runnable, delay);
////            }
////        }, delay);
        super.onResume();

                if (ContextCompat.checkSelfPermission(Splash.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    gonext();
                }

    }

    public void openPermissionScreen() {
        // startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", Splash.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}