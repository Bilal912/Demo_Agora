package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dinuscxj.progressbar.CircleProgressBar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Services extends AppCompatActivity {
    private CircleProgressBar mCustomProgressBar6;
    CircleImageView imageView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        imageView = findViewById(R.id.iv_header_img);
        mCustomProgressBar6 = findViewById(R.id.custom_progress6);

        mCustomProgressBar6.setProgress(50);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String pic = sharedPreferences.getString("picture","null");
        //Picasso.get().load(pic).into(imageView);
        Glide.with(Services.this)
                .load(pic)
                .centerCrop()
                .placeholder(R.drawable.user2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);


    }

    public void back(View view) {
        finish();
    }

    public void next(View view) {
    }
}