package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.edu_touch.edu_hunt.Adapter.My_Teacher_Adapter;
import com.edu_touch.edu_hunt.Model.payment_history_model;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Login extends AppCompatActivity {
EditText Email,Password;
LottieAnimationView animationView;
SharedPreferences sharedPreferences;
SharedPreferences.Editor editors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        animationView = findViewById(R.id.anime);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    }

    public void Register(View view) {
        startActivity(new Intent(Login.this,Register.class));
    }

    public void submit(View view) {

        if (TextUtils.isEmpty(Email.getText().toString())){
            makeText(Login.this, "Email is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Password.getText().toString())){
            makeText(Login.this, "Password is required", LENGTH_SHORT).show();
        }
        else {
            getData();
        }

    }

    private void getData() {
        final AlertDialog loading = new ProgressDialog(Login.this);
        loading.setMessage("Please Wait...");
        loading.setCancelable(false);
        //loading.show();
        animationView.setVisibility(View.VISIBLE);

        Map<String, String> params = new Hashtable<String, String>();
        params.put("email_id",Email.getText().toString().trim());
        params.put("password",Password.getText().toString().trim());
        params.put("device_id", FirebaseInstanceId.getInstance().getToken());

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_login, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error_code");

                    if (code.equals("200")){

                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);

                            editors = sharedPreferences.edit();
                            editors.putString("id", object.getString("id"));
                            editors.putString("address", object.getString("address"));
                            editors.putString("name", object.getString("name"));
                            editors.putString("phone", object.getString("contact_no"));
                            editors.putString("email", object.getString("email_id"));
                            editors.putString("status", object.getString("status"));
                            editors.putString("created_date", object.getString("created_date"));
                            editors.putString("password", object.getString("password"));
                            editors.putString("picture", Constant.image_url.concat(object.getString("profile_image")));
                            editors.putString("class", object.getString("class"));

                            editors.putString("board",object.getString("board"));
                            editors.putString("class_group",object.getString("class_group"));

                            editors.putString("city", object.getString("city"));
                            editors.putString("state", object.getString("state"));
                            editors.putString("zip", object.getString("zip"));

                            //editors.putString("name", object.getString("device_id"));

                            Checkuser(object.getString("id"));

                        }
                    }
                    else {
                        animationView.setVisibility(View.GONE);
                        Toasty.error(Login.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                    //  loading.dismiss();
                    e.printStackTrace();
                    animationView.setVisibility(View.GONE);
                    Toasty.error(Login.this, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                animationView.setVisibility(View.GONE);
                Toasty.error(Login.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(jsonRequest);

    }

    private void Checkuser(String id) {
        Map<String, String> params = new Hashtable<String, String>();
        params.put("std_id",id);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_checkinguser, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error code");

                    if (code.equals("200")){
                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            if (object.getString("login_status").equals("0")){
                                Intent i = new Intent(Login.this,Warning.class);
                                startActivity(i);
                                finish();
                            }
                            else {
                                editors.apply();
                                Intent intent = new Intent(Login.this,Home.class);
                                startActivity(intent);
                                finish();
                                Toasty.success(Login.this, "Login Successfully", Toast.LENGTH_SHORT, true).show();

                            }
                        }
                    }
                    else {
//                        textView.setVisibility(View.VISIBLE);
//                        animationView.setVisibility(View.GONE);
//                        Toasty.error(My_teacher.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {

//                    textView.setVisibility(View.VISIBLE);
//                    animationView.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                animationView.setVisibility(View.GONE);

                Toasty.error(Login.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(jsonRequest);

    }

    public void back(View view) {
        finish();
    }

    public void forget(View view) {
        Intent intent = new Intent(Login.this,Forgot.class);
        startActivity(intent);
    }
}