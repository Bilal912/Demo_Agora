package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu_touch.edu_hunt.Adapter.Book_Now_Adapter;
import com.edu_touch.edu_hunt.Adapter.Subject_Adapter;
import com.edu_touch.edu_hunt.Model.select_model;
import com.edu_touch.edu_hunt.Model.subject_model;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Teacher_detail extends AppCompatActivity implements clicky {
RecyclerView recyclerView;
SharedPreferences sharedPreferences;

Book_Now_Adapter adapter;
TextView Name,City,Address,Experience,Qualification;
String name,city,address,exp,quali,fees,board,subject,classes,code,id,image,subject_id,class_id,board_id;
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

        subject_id = getIntent().getStringExtra("subject_id");
        class_id = getIntent().getStringExtra("class_id");
        board_id = getIntent().getStringExtra("board_id");

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

            String[] subjecty_id = subject_id.split("\\|");
            String[] boardy_id = board_id.split("\\|");
            String[] classy_id = class_id.split("\\|");

            for (int i=0;i<feey.length;i++){
                select_model s = new select_model();
                s.setId(id);
                s.setBoards(boardy[i]);
                s.setClass_name(classy[i]);
                s.setFees(feey[i]);
                s.setSubjects(subjecty[i]);

                s.setBoards_id(boardy_id[i]);
                s.setClass_id(classy_id[i]);
                s.setSubjects_id(subjecty_id[i]);
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
            s.setBoards_id(board_id);
            s.setClass_id(class_id);
            s.setSubjects_id(subject_id);

            arrayList.add(s);
        }

        adapter = new Book_Now_Adapter(Teacher_detail.this, arrayList,Teacher_detail.this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onclicky(String s, String c, String b,String fee) {

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        String code = "";
        for (int z=0;z<4;z++) {
            final int min = 0;
            final int max = 9;
            final int random = new Random().nextInt((max - min) + 1) + min;
            code = code.concat(String.valueOf(random));
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateobj = new Date();
        String newDateStr = df.format(dateobj);

        //Toast.makeText(this, newDateStr+""+code+"- "+sharedPreferences.getString("id","null"), Toast.LENGTH_SHORT).show();

        Context context = Teacher_detail.this;
        final AlertDialog loading = new ProgressDialog(context);
        loading.setMessage("Booking....");
        loading.setCancelable(false);
        loading.show();

        Map<String, String> params = new Hashtable<String, String>();
        params.put("std_id",sharedPreferences.getString("id","null"));
        params.put("teacher_id",id);
        params.put("subject_id",s);
        params.put("class_group_id",c);
        params.put("booking_date",newDateStr);
        params.put("teacher_otp",code);
        params.put("amount",fee);
        params.put("starting_date","000000");
        params.put("user_subscription_status",sharedPreferences.getString("status","null"));

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_booking_teacher, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error_code");
                    if (code.equals("200")){

                        //Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
                        loading.dismiss();
                        Teacher_detail.ViewDialog1 alert = new Teacher_detail.ViewDialog1();
                        alert.showDialog(Teacher_detail.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", "1");
                        editor.apply();

                    }
                    else {
                        loading.dismiss();
                        Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                    //Toasty.error(context, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toasty.error(context, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonRequest);

    }
    public class ViewDialog1 {
        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialogbox);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
        }
    }

}