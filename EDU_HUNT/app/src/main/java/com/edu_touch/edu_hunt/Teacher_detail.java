package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu_touch.edu_hunt.Adapter.Book_Now_Adapter;
import com.edu_touch.edu_hunt.Adapter.Subject_Adapter;
import com.edu_touch.edu_hunt.Model.select_model;
import com.edu_touch.edu_hunt.Model.subject_model;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Teacher_detail extends AppCompatActivity {
RecyclerView recyclerView;
Book_Now_Adapter adapter;
TextView Name,City,Address,Experience,Qualification;
String name,city,address,exp,quali,fees,board,subject,classes,code,id,image;
ArrayList<select_model> arrayList;
CircleImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);

        arrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_id);
        Name=findViewById(R.id.t_name);
        City = findViewById(R.id.t_city);
        Address = findViewById(R.id.t_address);
        Experience = findViewById(R.id.t_exp);
        Qualification = findViewById(R.id.t_quali);
        imageView = findViewById(R.id.image_id);

        image = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        city = getIntent().getStringExtra("city");
        address = getIntent().getStringExtra("address");
        exp = getIntent().getStringExtra("experience");
        quali = getIntent().getStringExtra("qualification");
        fees = getIntent().getStringExtra("fee");
        board = getIntent().getStringExtra("board");
        subject = getIntent().getStringExtra("subject");

        id = getIntent().getStringExtra("id");
        code = getIntent().getStringExtra("code");
        classes = getIntent().getStringExtra("class");

        Glide.with(Teacher_detail.this)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.user2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Teacher_detail.this,Show_image.class);
                i.putExtra("image",image);
                startActivity(i);
            }
        });

        Name.setText(name);
        City.setText("City : "+city);
        Address.setText("Address : "+ Html.fromHtml(address).toString());
        Qualification.setText("Qualification : "+quali);
        Experience.setText("Experience : "+exp);


        LinearLayoutManager layoutManager = new LinearLayoutManager(Teacher_detail.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Teacher_detail.this) {
                    private static final float SPEED = 2000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        getData();

    }

    private void getData() {

        if (subject.contains("|")){
            String[] subjecty = subject.split("\\|");
            String[] boardy = board.split("\\|");
            String[] feey = fees.split("\\|");
            String[] classy = classes.split("\\|");

            for (int i=0;i<feey.length;i++){
                select_model s = new select_model();
                s.setId(id);
                s.setBoards(boardy[i]);
                s.setClass_name(classy[i]);
                s.setFees(feey[i]);
                s.setSubjects(subjecty[i]);
                arrayList.add(s);
            }
        }
        else {
            select_model s = new select_model();
            s.setId(id);
            s.setBoards(board);
            s.setClass_name(classes);
            s.setFees(fees);
            s.setSubjects(subject);
            arrayList.add(s);
        }

        adapter = new Book_Now_Adapter(Teacher_detail.this, arrayList);
        recyclerView.setAdapter(adapter);

    }
}