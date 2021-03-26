package com.edu_touch.edu_hunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
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
import com.edu_touch.edu_hunt.Adapter.Small_Teacher_Adapter;
import com.edu_touch.edu_hunt.Adapter.Small_top_Teacher_Adapter;
import com.edu_touch.edu_hunt.Adapter.Subject_Adapter;
import com.edu_touch.edu_hunt.Adapter.Teacher_Adapter;
import com.edu_touch.edu_hunt.Model.subject_model;
import com.edu_touch.edu_hunt.Model.teacher_model;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Listing extends AppCompatActivity {
CircleImageView imageView;
ShimmerFrameLayout shimme_subject,subjecty_shimmer,teacher_shimmer;
RecyclerView recyclerView,rec_teacher,top_teachers;
ArrayList<subject_model> sub_model;
ArrayList<teacher_model> teacher_models;
Small_Teacher_Adapter adapter;
Small_top_Teacher_Adapter top_teacher_adapter;
Subject_Adapter subject_adapter;
SharedPreferences sharedPreferences;
TextView no_data,no_data1,no_data3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        no_data3 = findViewById(R.id.no_data3);
        no_data = findViewById(R.id.no_data);
        no_data1 = findViewById(R.id.no_data1);
        imageView = findViewById(R.id.iv_header_img);
        teacher_shimmer = findViewById(R.id.shimmer_teacher);
        subjecty_shimmer=findViewById(R.id.shimmer_suby);

        shimme_subject = findViewById(R.id.shimmer_subject);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        top_teachers = findViewById(R.id.rec_top_teacher);
        teacher_models = new ArrayList<>();
        rec_teacher = findViewById(R.id.rec_allteachers);
        recyclerView = findViewById(R.id.rec_subject);
        sub_model = new ArrayList<>();
        String pic = sharedPreferences.getString("picture","null");
        //Picasso.get().load(pic).into(imageView);
        Glide.with(Listing.this)
                .load(pic)
                .centerCrop()
                .placeholder(R.drawable.user2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(Listing.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Listing.this) {
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
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(Listing.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Listing.this) {
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
        rec_teacher.setLayoutManager(layoutManager2);
        rec_teacher.setHasFixedSize(true);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(Listing.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Listing.this) {
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
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        top_teachers.setLayoutManager(layoutManager3);
        top_teachers.setHasFixedSize(true);

        if (Home.distance == 0){
            getingfee();
        }
        else {
            getTeachers();
        }

        getSubject();

    }

    private void getTeachers() {

        Map<String, String> params = new Hashtable<String, String>();

        params.put("lang",sharedPreferences.getString("login_lang","0"));
        params.put("lat",sharedPreferences.getString("login_lat","0"));

        params.put("class_id",sharedPreferences.getString("class","0"));
        params.put("city",sharedPreferences.getString("city","null"));

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_teacher_listing, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error code");

                    if (code.equals("200")){

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

                                s.setDis(Float.parseFloat(String.format("%.2f", dis)));

                                s.setDistance(IntValue);

                                s.setClass_id(object.getString("class_id"));
                                s.setBoards_id(object.getString("boards_id"));
                                s.setSubjects_id(object.getString("subjects_id"));

//                                Gson gson = new Gson();
//                                Type listType = new TypeToken<List<teacher_model>>() {
//                                }.getType();
//                                teacher_models = gson.fromJson(response.getString("teachers"), listType);

                                teacher_models.add(s);
                            }

                        }
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<teacher_model>>() {
//                        }.getType();
//                        teacher_models = gson.fromJson(response.getString("teachers"), listType);


                        if (teacher_models == null){
                            no_data.setVisibility(View.VISIBLE);
                        }
                        else {

                            Collections.sort(teacher_models, new Comparator<teacher_model>() {
                                @Override
                                public int compare(teacher_model lhs, teacher_model rhs) {
                                    return Float.compare(lhs.getDis(), rhs.getDis());
                                }
                            });

                            top_teacher_adapter = new Small_top_Teacher_Adapter(Listing.this, teacher_models);
                            top_teachers.setAdapter(top_teacher_adapter);

                            adapter = new Small_Teacher_Adapter(Listing.this, teacher_models);
                            rec_teacher.setAdapter(adapter);
                        }


                        shimme_subject.stopShimmer();
                        shimme_subject.setVisibility(View.GONE);
                        teacher_shimmer.stopShimmer();
                        teacher_shimmer.setVisibility(View.GONE);

                    }
                    else {

                        no_data.setVisibility(View.VISIBLE);
                        no_data1.setVisibility(View.VISIBLE);

                        Toasty.error(Listing.this, "No Data Found", Toast.LENGTH_SHORT, true).show();
                        shimme_subject.stopShimmer();
                        shimme_subject.setVisibility(View.GONE);
                        teacher_shimmer.stopShimmer();
                        teacher_shimmer.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    shimme_subject.stopShimmer();
                    shimme_subject.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                    no_data1.setVisibility(View.VISIBLE);

                    teacher_shimmer.stopShimmer();
                    teacher_shimmer.setVisibility(View.GONE);

                    e.printStackTrace();
//                    animationView.setVisibility(View.GONE);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                shimme_subject.stopShimmer();
//                shimme_subject.setVisibility(View.GONE);
//                teacher_shimmer.stopShimmer();
//                teacher_shimmer.setVisibility(View.GONE);

                Toasty.error(Listing.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Listing.this);
        queue.add(jsonRequest);

    }

    private void getSubject() {

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
                            s.setId(object.getString("id"));
//                            s.setContent(object.getString("content"));
//                            s.setCreated_at(object.getString("created_at"));
//                            s.setStatus(object.getString("status"));
                            s.setTitle(object.getString("title"));
                            sub_model.add(s);
                        }

                        subject_adapter = new Subject_Adapter(Listing.this, sub_model);
                        recyclerView.setAdapter(subject_adapter);

                        subjecty_shimmer.stopShimmer();
                        subjecty_shimmer.setVisibility(View.GONE);
                    }
                    else {
                        //animationView.setVisibility(View.GONE);
                        no_data3.setVisibility(View.GONE);
                        Toasty.error(Listing.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                    no_data3.setVisibility(View.GONE);
                    subjecty_shimmer.stopShimmer();
                    subjecty_shimmer.setVisibility(View.GONE);
                    //  loading.dismiss();
                    e.printStackTrace();
                    Toasty.error(Listing.this, "No Data Found", Toast.LENGTH_SHORT, true).show();

                    //animationView.setVisibility(View.GONE);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                //animationView.setVisibility(View.GONE);
                no_data3.setVisibility(View.GONE);
                subjecty_shimmer.stopShimmer();
                subjecty_shimmer.setVisibility(View.GONE);
                Toasty.error(Listing.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Listing.this);
        queue.add(jsonRequest);


    }

    public void Success(View view) {
        ViewDialog1 alert = new ViewDialog1();
        alert.showDialog(Listing.this);
    }

    public void back(View view) {
        finish();
    }

    public void view_all(View view) {
        Intent i = new Intent(Listing.this, Show_Teachers.class);
        i.putExtra("id","ViewAll");
        startActivity(i);
    }

    public void filters(View view) {
        Intent i = new Intent(Listing.this, Filter.class);
        startActivity(i);
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


    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        shimme_subject.stopShimmer();
        subjecty_shimmer.stopShimmer();
        teacher_shimmer.stopShimmer();

    }

    @Override
    public void onResume() {
        super.onResume();
        teacher_shimmer.startShimmer();
        subjecty_shimmer.startShimmer();
        shimme_subject.startShimmer();

    }


    public void getLocation(){

        SharedPreferences.Editor leditor=sharedPreferences.edit();

        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Listing.this);

        if(ActivityCompat.checkSelfPermission(Listing.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Listing.this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
            },2);

        }else {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener( new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {

                                leditor.putString("lat", String.valueOf(location.getLatitude()));
                                leditor.putString("lang", String.valueOf(location.getLongitude()));

//                                Geocoder geocoder = new Geocoder(Splash.this, Locale.ENGLISH);
//                                try {
//                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude()
//                                            , location.getLongitude()
//                                            , 1);
//
//
//                                    if(addresses.size()>0) {
//
//                                        city = addresses.get(0).getSubAdminArea();
//                                        if(city == null) {
//                                        }
//                                        else {
//                                            leditor.putString("city", city);
//                                        }
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }

                                leditor.apply();

                                location.reset();
                            }
                            else{
//                                Toast.makeText(Splash.this, "No Data Found"
//                                        , Toast.LENGTH_SHORT).show();
                            }

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

        }
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


    private void getingfee() {
        Context context = Listing.this;
        final android.app.AlertDialog loading = new ProgressDialog(context);
        loading.setMessage("Getting Info....");
        loading.setCancelable(false);
        //loading.show();

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_checkingfee, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String code = response.getString("error code");
                    if (code.equals("200")){

                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);

                            Home.distance = Integer.parseInt(object.getString("nearest"));

                            getTeachers();

                        }

                    }
                    else {
                        loading.dismiss();
                        //Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
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

}