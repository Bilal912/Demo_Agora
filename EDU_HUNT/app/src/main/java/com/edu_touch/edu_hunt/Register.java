package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void submit(View view) {
        startActivity(new Intent(Register.this,Phoneno.class));
    }

    public void back(View view) {
        finish();
    }

    public void Login(View view) {
        startActivity(new Intent(Register.this,Login.class));
    }
}