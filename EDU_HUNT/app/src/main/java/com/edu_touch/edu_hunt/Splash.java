package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Splash extends AppCompatActivity {
Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        t = new Thread() {
            public void run() {
                try {
                    t.sleep(2500);
                    SharedPreferences editors = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
                    String Email = editors.getString("email","Null");
                    if (Email.equals("Null")) {
                        Intent i = new Intent(Splash.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(Splash.this, Home.class);
                        startActivity(i);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };t.start();

    }
}