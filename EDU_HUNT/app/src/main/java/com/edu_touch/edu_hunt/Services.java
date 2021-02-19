package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dinuscxj.progressbar.CircleProgressBar;

public class Services extends AppCompatActivity {
    private CircleProgressBar mCustomProgressBar6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        mCustomProgressBar6 = findViewById(R.id.custom_progress6);

        mCustomProgressBar6.setProgress(50);



    }

    public void back(View view) {
        finish();
    }

    public void next(View view) {
    }
}