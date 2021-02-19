package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Paymenthistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymenthistory);
    }

    public void back(View view) {
        finish();
    }

    public void next(View view) {
        startActivity(new Intent(Paymenthistory.this,Services.class));
    }
}