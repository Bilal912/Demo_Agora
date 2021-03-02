package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.edu_touch.edu_hunt.Adapter.Subject_Adapter;
import com.edu_touch.edu_hunt.Adapter.Teacher_Adapter;
import com.edu_touch.edu_hunt.Model.teacher_model;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Show_Teachers extends AppCompatActivity {
TextView textView,no_data;
LottieAnimationView animationView;
RecyclerView recyclerView;
ArrayList<teacher_model> arrayList;
Teacher_Adapter teacher_adapter;
String sclass,id;
SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__teachers);

        animationView = findViewById(R.id.anime);
        no_data = findViewById(R.id.no_data);
        textView = findViewById(R.id.icon);

        recyclerView = findViewById(R.id.rec_teacher);

        LinearLayoutManager layoutManager = new LinearLayoutManager(Show_Teachers.this) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Show_Teachers.this) {
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

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        sclass = sharedPreferences.getString("class","null");
        id = getIntent().getStringExtra("id");

        if (id.equals("ViewAll")){
            getAllteachers();
        }
        else {
            textView.setText("Search Teacher By "+getIntent().getStringExtra("title"));
            getteacherbysubject(id,sclass);
        }

    }

    private void getAllteachers() {
        Map<String, String> params = new Hashtable<String, String>();
        params.put("lang",sharedPreferences.getString("lang","0"));
        params.put("lat",sharedPreferences.getString("lat","0"));
        params.put("class_id",sharedPreferences.getString("class","0"));

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_teacher_listing, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error code");

                    if (code.equals("200")){

                        no_data.setVisibility(View.GONE);

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<teacher_model>>() {
                        }.getType();
                        arrayList = gson.fromJson(response.getString("teachers"), listType);

                        teacher_adapter = new Teacher_Adapter(Show_Teachers.this, arrayList);
                        recyclerView.setAdapter(teacher_adapter);
                        animationView.setVisibility(View.GONE);
                    }
                    else {
                        animationView.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                        Toasty.error(Show_Teachers.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {

                    animationView.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                no_data.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.GONE);
                Toasty.error(Show_Teachers.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Show_Teachers.this);
        queue.add(jsonRequest);

    }

    private void getteacherbysubject(String Id, String sclass) {

        Map<String, String> params = new Hashtable<String, String>();
        params.put("lang",sharedPreferences.getString("lang","0"));
        params.put("lat",sharedPreferences.getString("lat","0"));
        params.put("subject_id",Id);
        params.put("class_id",sharedPreferences.getString("class","0"));

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_teacher_listing, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error code");

                    if (code.equals("200")){

                        no_data.setVisibility(View.GONE);
//                        JSONArray jsonArray = response.getJSONArray("teachers");

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<teacher_model>>() {
                        }.getType();
                        arrayList = gson.fromJson(response.getString("teachers"), listType);

                        teacher_adapter = new Teacher_Adapter(Show_Teachers.this, arrayList);
                        recyclerView.setAdapter(teacher_adapter);

                        animationView.setVisibility(View.GONE);
                    }
                    else {
                        animationView.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
//                        animationView.setVisibility(View.GONE);
                        Toasty.error(Show_Teachers.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {

                    animationView.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                    //  loading.dismiss();
                    e.printStackTrace();
//                    animationView.setVisibility(View.GONE);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                no_data.setVisibility(View.VISIBLE);
                //loading.dismiss();
                animationView.setVisibility(View.GONE);
                Toasty.error(Show_Teachers.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Show_Teachers.this);
        queue.add(jsonRequest);


    }

    public void back(View view) {finish();
    }
}