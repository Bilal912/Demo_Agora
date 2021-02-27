package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu_touch.edu_hunt.Adapter.Fee_Adapter;
import com.edu_touch.edu_hunt.Adapter.My_Teacher_Adapter;
import com.edu_touch.edu_hunt.Model.fee_model;
import com.edu_touch.edu_hunt.Model.payment_history_model;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Fees extends AppCompatActivity {
    RecyclerView recyclerView;
    LottieAnimationView animationView;
    SharedPreferences sharedPreferences;
    ArrayList<fee_model> arrayList;
    Fee_Adapter adapter;
    TextView textView;
    CircleImageView imageView;

    public static int loady = 0;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        imageView = findViewById(R.id.iv_header_img);
        animationView = findViewById(R.id.anime);

        textView = findViewById(R.id.no_data);
        arrayList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String pic = sharedPreferences.getString("picture","null");
        //Picasso.get().load(pic).into(imageView);
        Glide.with(Fees.this)
                .load(pic)
                .centerCrop()
                .placeholder(R.drawable.user2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);

        recyclerView = findViewById(R.id.recycler_fees);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(Fees.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Fees.this) {
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
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.setHasFixedSize(true);

        getFees();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                arrayList.clear();
                getFees();

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 2000); // Delay in millis
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



    }

    private void getFees() {
        String avy = sharedPreferences.getString("id","null");
        Map<String, String> params = new Hashtable<String, String>();
        params.put("std_id",avy);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_getstudentfee, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error code");

                    if (code.equals("200")){

                        textView.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<fee_model>>() {
                        }.getType();
                        arrayList = gson.fromJson(response.getString("teachers"), listType);

                        adapter = new Fee_Adapter(Fees.this, arrayList);
                        recyclerView.setAdapter(adapter);
                        animationView.setVisibility(View.GONE);
                    }
                    else {
                        textView.setVisibility(View.VISIBLE);
                        animationView.setVisibility(View.GONE);
                        Toasty.error(Fees.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {

                    textView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                textView.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.GONE);
                Toasty.error(Fees.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Fees.this);
        queue.add(jsonRequest);
        
    }

    public void back(View view) {
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (loady == 1){
            animationView.setVisibility(View.VISIBLE);
            arrayList.clear();
            loady = 0;
            getFees();
        }
    }

}