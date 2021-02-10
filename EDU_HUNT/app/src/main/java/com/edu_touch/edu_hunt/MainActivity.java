package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Login(View view) {
        i = new Intent(MainActivity.this,Login.class);
        startActivity(i);
    }

    public void Signup(View view) {
        i = new Intent(MainActivity.this,Register.class);
        startActivity(i);
    }

    public void avy(View view) {
        i = new Intent(MainActivity.this,otp.class);
        startActivity(i);
    }

    public void phone(View view) {
        i = new Intent(MainActivity.this,Phoneno.class);
        startActivity(i);
    }
}