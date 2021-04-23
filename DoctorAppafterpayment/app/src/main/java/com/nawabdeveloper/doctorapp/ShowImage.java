package com.nawabdeveloper.doctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ShowImage extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        getSupportActionBar().hide();

        imageView = findViewById(R.id.image);

        Picasso.get().load(getIntent().getStringExtra(Constant.image)).into(imageView);

    }
}