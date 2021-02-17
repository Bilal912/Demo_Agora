package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.edu_touch.edu_hunt.volley.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Forgot extends AppCompatActivity {
EditText editText;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        animationView = (LottieAnimationView) findViewById(R.id.anime);
        editText = findViewById(R.id.femail);
    }

    public void forgot(View view) {

        if (TextUtils.isEmpty(editText.getText().toString())){
            makeText(Forgot.this, "Email is required", LENGTH_SHORT).show();
        }
        else {
            getData(editText.getText().toString());
        }

    }

    private void getData(String mail) {
        animationView.setVisibility(View.VISIBLE);
        Map<String, String> params = new Hashtable<String, String>();
        params.put("email_id",mail);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_forgot, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String message = response.getString("message");
                    String code = response.getString("error_code");

                    if (code.equals("200")){

                        Toasty.success(Forgot.this, message, Toast.LENGTH_SHORT, true).show();
                        finish();
                    }
                    else {
                        Toasty.error(Forgot.this, message, Toast.LENGTH_SHORT, true).show();
                        animationView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    //  loading.dismiss();
                    e.printStackTrace();
                    animationView.setVisibility(View.GONE);
                    Toasty.error(Forgot.this, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                animationView.setVisibility(View.GONE);
                Toasty.error(Forgot.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Forgot.this);
        queue.add(jsonRequest);


    }
}