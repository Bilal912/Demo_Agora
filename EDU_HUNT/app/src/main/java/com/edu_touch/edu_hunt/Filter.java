package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edu_touch.edu_hunt.Adapter.Teacher_Adapter;
import com.edu_touch.edu_hunt.Model.subject_model;
import com.edu_touch.edu_hunt.Model.teacher_model;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
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

import static android.widget.Toast.LENGTH_SHORT;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Filter extends AppCompatActivity {
Spinner classy,subjecty;
    ShimmerFrameLayout teacher_shimmer;
TextView no_data;
RecyclerView recyclerView;
ArrayList<teacher_model> arrayList;
SharedPreferences sharedPreferences;
    Teacher_Adapter teacher_adapter;
ArrayList<String> classes,subjects,class_id,sub_id;
String lat,lon;
int a=0,b=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        teacher_shimmer = findViewById(R.id.shimmer_teacher);

        no_data = findViewById(R.id.no_data);
        arrayList = new ArrayList<>();
        classes = new ArrayList<>();
        subjects = new ArrayList<>();
        class_id = new ArrayList<>();

        lat = sharedPreferences.getString("lat","0");
        lon = sharedPreferences.getString("long","0");
        sub_id = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_id);
        classy = findViewById(R.id.spinner_class);
        subjecty = findViewById(R.id.spinner_subject);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Filter.this) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Filter.this) {
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

        getclassspinnner();
        getsubjectspinner();

        classy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (a==0){
                    a=1;
                }
                else {
//                    Toast.makeText(Filter.this, class_id.get(classy.getSelectedItemPosition())+" - " +
//                            ""+ sub_id.get(subjecty.getSelectedItemPosition()), LENGTH_SHORT).show();

                    getTeacherfilter(class_id.get(classy.getSelectedItemPosition()).trim(), sub_id.get(subjecty.getSelectedItemPosition()).trim());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        subjecty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                if (b==0){
//                    b=1;
//                }
//                else {
//                    Toast.makeText(Filter.this, class_id.get(classy.getSelectedItemPosition())+" - " +
//                            ""+ sub_id.get(subjecty.getSelectedItemPosition()), LENGTH_SHORT).show();
                    getTeacherfilter(class_id.get(classy.getSelectedItemPosition()).trim(), sub_id.get(subjecty.getSelectedItemPosition()).trim());
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

    }

    private void getTeacherfilter(String c_id, String s_id) {

        final AlertDialog loading = new ProgressDialog(Filter.this);
        loading.setMessage("Getting Teacher....");
        loading.setCancelable(false);
        //loading.show();

        no_data.setVisibility(View.GONE);
        teacher_shimmer.startShimmer();
        teacher_shimmer.setVisibility(View.VISIBLE);

        Map<String, String> params = new Hashtable<String, String>();
        params.put("lang",lon);
        params.put("lat",lat);
        params.put("subject_id",s_id);
        params.put("class_id",c_id);
        params.put("city",sharedPreferences.getString("city","0"));

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_teacher_listing, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    arrayList.clear();
                    arrayList = new ArrayList<>();

                    String message = response.getString("message");
                    String code = response.getString("error code");

                    if (code.equals("200")){

                        no_data.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<teacher_model>>() {
                        }.getType();
                        arrayList = gson.fromJson(response.getString("teachers"), listType);

                        teacher_adapter = new Teacher_Adapter(Filter.this, arrayList);
                        recyclerView.setAdapter(teacher_adapter);
                        teacher_adapter.notifyDataSetChanged();

                        loading.dismiss();
                        teacher_shimmer.stopShimmer();
                        teacher_shimmer.setVisibility(View.GONE);

                    }
                    else {
                        teacher_shimmer.stopShimmer();
                        teacher_shimmer.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.GONE);
                        loading.dismiss();
                        no_data.setVisibility(View.VISIBLE);
                        Toasty.error(Filter.this, message, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    teacher_shimmer.stopShimmer();
                    teacher_shimmer.setVisibility(View.GONE);

                    recyclerView.setVisibility(View.GONE);
                    loading.dismiss();
                    no_data.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                teacher_shimmer.stopShimmer();
//                teacher_shimmer.setVisibility(View.GONE);
//
//                no_data.setVisibility(View.VISIBLE);
                loading.dismiss();
                Toasty.error(Filter.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Filter.this);
        queue.add(jsonRequest);

    }

    private void getsubjectspinner() {

        Map<String, String> params = new Hashtable<String, String>();
        params.put("class_id",sharedPreferences.getString("class","0"));
        params.put("board",sharedPreferences.getString("board","0"));
        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_getsubjects, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error code");

                    if (code.equals("200")){

                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            subject_model s = new subject_model();
                            sub_id.add(object.getString("id"));
                            subjects.add(object.getString("title"));
                        }

                        subjecty.setAdapter(new ArrayAdapter<String>(Filter.this,
                                android.R.layout.simple_dropdown_item_1line,
                                subjects));

                    }
                    else {
                        //animationView.setVisibility(View.GONE);
                        Toasty.error(Filter.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {

                    //  loading.dismiss();
                    e.printStackTrace();
                    Toasty.error(Filter.this, "No Data Found", Toast.LENGTH_SHORT, true).show();

                    //animationView.setVisibility(View.GONE);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                //animationView.setVisibility(View.GONE);

                Toasty.error(Filter.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Filter.this);
        queue.add(jsonRequest);


    }

    private void getclassspinnner() {
        final android.app.AlertDialog loading = new ProgressDialog(Filter.this);
        loading.setMessage("Please Wait a Moment...");
//        loading.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_getclasses
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String code = response.getString("error code");

                    if (code.equals("200")) {

                        String current_class = sharedPreferences.getString("class","0");

                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);

                            if (object.getString("id").equals(current_class)){
                                classes.add(object.getString("name"));
                                class_id.add(object.getString("id"));
                            }

                        }

                        classy.setAdapter(new ArrayAdapter<String>(Filter.this,
                                android.R.layout.simple_dropdown_item_1line,
                                classes));
                        loading.dismiss();
                    }
                    else {
                        loading.dismiss();
                    }

                } catch (JSONException e) {
                    loading.dismiss();
                    Toast.makeText(Filter.this,"Internet Issue", LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Filter.this, "Connection Timed Out" ,Toast.LENGTH_LONG).show();
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonRequest);

    }

    public void back(View view) {
        finish();
    }
    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        teacher_shimmer.stopShimmer();

    }

    @Override
    public void onResume() {
        super.onResume();
        teacher_shimmer.startShimmer();
    }
}