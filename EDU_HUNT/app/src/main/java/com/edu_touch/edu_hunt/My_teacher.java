package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

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
import com.edu_touch.edu_hunt.Adapter.My_Teacher_Adapter;
import com.edu_touch.edu_hunt.Adapter.Payment_History_Adapter;
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

import es.dmoral.toasty.Toasty;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class My_teacher extends AppCompatActivity {
    RecyclerView recyclerView;
    LottieAnimationView animationView;
    SharedPreferences sharedPreferences;
    ArrayList<payment_history_model> arrayList;
    My_Teacher_Adapter adapter;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teacher);

        animationView = findViewById(R.id.anime);

        textView = findViewById(R.id.no_data);
        arrayList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        recyclerView = findViewById(R.id.recycler_myteacher);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(My_teacher.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(My_teacher.this) {
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

        getmyteacher();


    }

    private void getmyteacher() {
        String avy = sharedPreferences.getString("id","null");
        Map<String, String> params = new Hashtable<String, String>();
        params.put("student_id",avy);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_mybookings, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error code");
                    String imagelink = response.getString("base_url");

                    if (code.equals("200")){

                        textView.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<payment_history_model>>() {
                        }.getType();
                        arrayList = gson.fromJson(response.getString("teachers"), listType);

                        adapter = new My_Teacher_Adapter(My_teacher.this, arrayList,imagelink);
                        recyclerView.setAdapter(adapter);
                        animationView.setVisibility(View.GONE);
                    }
                    else {
                        textView.setVisibility(View.VISIBLE);
                        animationView.setVisibility(View.GONE);
                        Toasty.error(My_teacher.this, message, Toast.LENGTH_SHORT, true).show();
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
                Toasty.error(My_teacher.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(My_teacher.this);
        queue.add(jsonRequest);

    }

    public void back(View view) {
        finish();
    }
}