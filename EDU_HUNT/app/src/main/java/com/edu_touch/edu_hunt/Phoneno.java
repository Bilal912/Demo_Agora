package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Phoneno extends AppCompatActivity {
EditText Phone;
    public static LottieAnimationView animationView;
    String name,email,pass,classes,address,city,state,zip,class_group,board,subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneno);
        animationView = (LottieAnimationView) findViewById(R.id.anime);

        Phone = findViewById(R.id.number);

//        subject = getIntent().getStringExtra("subject");
        board = getIntent().getStringExtra("board");
        //class_group = getIntent().getStringExtra("class_group");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email_id");
        pass = getIntent().getStringExtra("password");
        address = getIntent().getStringExtra("address");
        classes = getIntent().getStringExtra("class");

        zip = getIntent().getStringExtra("zip");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
    }

    public void submit(View view) {

        if (TextUtils.isEmpty(Phone.getText().toString())){
            makeText(Phoneno.this, "Number is required", LENGTH_SHORT).show();
        }
        else {
            getData(Phone.getText().toString());
        }
    }

    private void getData(String number) {

        animationView.setVisibility(View.VISIBLE);
        Map<String, String> params = new Hashtable<String, String>();
        params.put("number","+91".concat(number));

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_sendotp, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error_code");

                    if (code.equals("200")){

                        Toasty.success(Phoneno.this, message, Toast.LENGTH_SHORT, true).show();

                        int j = response.getInt("code");
                        Intent i = new Intent(Phoneno.this,otp.class);
                        i.putExtra("name",name);
                        i.putExtra("board",board);
                        //i.putExtra("class_group",class_group);
                        //i.putExtra("subject",subject);

                        i.putExtra("email_id",email);
                        i.putExtra("password",pass);
                        i.putExtra("class",classes);
                        i.putExtra("address",address);
                        i.putExtra("zip",zip);
                        i.putExtra("city",city);
                        i.putExtra("state",state);

                        i.putExtra("number","+91".concat(number));
                        i.putExtra("code",String.valueOf(j));
                        startActivity(i);
                    }
                    else {
                        Toasty.error(Phoneno.this, message, Toast.LENGTH_SHORT, true).show();
                        animationView.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    //  loading.dismiss();
                    e.printStackTrace();
                    animationView.setVisibility(View.GONE);
                    Toasty.error(Phoneno.this, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                animationView.setVisibility(View.GONE);
                Toasty.error(Phoneno.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Phoneno.this);
        queue.add(jsonRequest);


    }
}