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
import java.util.Collections;
import java.util.Comparator;
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

        subjects.add("Select Subject");

        lat = sharedPreferences.getString("login_lat","0");
        lon = sharedPreferences.getString("login_lang","0");
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

        if (sharedPreferences.getString("class_name","null").equals("null")){
            getclassspinnner();
        }
        else {
            classes.add(sharedPreferences.getString("class_name","null"));
            classy.setAdapter(new ArrayAdapter<String>(Filter.this,
                    android.R.layout.simple_dropdown_item_1line,
                    classes));
        }

        getsubjectspinner();

        subjecty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (subjecty.getSelectedItem().equals("Select Subject")){
                    teacher_shimmer.stopShimmer();
                    teacher_shimmer.setVisibility(View.GONE);
                }
                else {
                    try {
                        getTeacherfilter(sharedPreferences.getString("class", "null"), sub_id.get(subjecty.getSelectedItemPosition()-1));

                    } catch (Exception ignored) {
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
        params.put("board_id",sharedPreferences.getString("board","0"));

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

                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);

                            double dis = distance(Double.parseDouble(object.getString("google_lat")),
                                    Double.parseDouble(object.getString("google_long")),
                                    Double.parseDouble(sharedPreferences.getString("login_lat","0")),
                                    Double.parseDouble(sharedPreferences.getString("login_lang","0")));

                            int IntValue = (int) dis;

                            if (IntValue <= Home.distance){

                                teacher_model s = new teacher_model();
                                s.setId(object.getString("id"));
                                s.setAddress(object.getString("address"));
                                s.setTeacher_code(object.getString("teacher_code"));
                                s.setCity(object.getString("city"));
                                s.setQualification(object.getString("qualification"));
                                s.setExperience(object.getString("experience"));
                                s.setT_image(object.getString("t_image"));
                                s.setSubjects(object.getString("subjects"));
                                s.setBoards(object.getString("boards"));
                                s.setClass_name(object.getString("class_name"));
                                s.setFees(object.getString("fees"));
                                s.setTeacher_name(object.getString("teacher_name"));

                                s.setDistance(IntValue);

                                s.setClass_id(object.getString("class_id"));
                                s.setBoards_id(object.getString("boards_id"));
                                s.setSubjects_id(object.getString("subjects_id"));

                                s.setDis(Float.parseFloat(String.format("%.2f", dis)));

                                arrayList.add(s);
                            }

                        }

                        if (arrayList == null){
                            no_data.setVisibility(View.VISIBLE);
                        }
                        else {

                            Collections.sort(arrayList, new Comparator<teacher_model>() {
                                @Override
                                public int compare(teacher_model lhs, teacher_model rhs) {
                                    return Float.compare(lhs.getDis(), rhs.getDis());
                                }
                            });

                            teacher_adapter = new Teacher_Adapter(Filter.this, arrayList);
                            recyclerView.setAdapter(teacher_adapter);
                        }


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

    public double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return dist;

    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}