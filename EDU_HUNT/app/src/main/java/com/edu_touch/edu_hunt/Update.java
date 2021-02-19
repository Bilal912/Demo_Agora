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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Update extends AppCompatActivity {
    EditText name,phone,email,address,classes,password;
    SharedPreferences sharedPreferences;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        animationView = (LottieAnimationView) findViewById(R.id.anime);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.number);
        address = (EditText) findViewById(R.id.address);
        classes = (EditText) findViewById(R.id.classes);
        password = (EditText) findViewById(R.id.password);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name","null"));
        phone.setText(sharedPreferences.getString("phone","null"));
        email.setText(sharedPreferences.getString("email","null"));
        address.setText(sharedPreferences.getString("address","null"));
        classes.setText(sharedPreferences.getString("class","null"));
        password.setText(sharedPreferences.getString("password","null"));

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Update.this, "You cannot edit your Email", Toast.LENGTH_SHORT).show();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Update.this, "You cannot edit your Phone", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void edit(View view) {
        if (TextUtils.isEmpty(name.getText().toString())){
            makeText(Update.this, "Name is required", LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(email.getText().toString())){
//            makeText(Update.this, "Email is required", LENGTH_SHORT).show();
//        }
//        else if (TextUtils.isEmpty(phone.getText().toString())){
//            makeText(Update.this, "Contact Number is required", LENGTH_SHORT).show();
//        }
        else if (TextUtils.isEmpty(classes.getText().toString())){
            makeText(Update.this, "Class is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address.getText().toString())){
            makeText(Update.this, "Address is required", LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(password.getText().toString())){
//            makeText(Update.this, "Password is required", LENGTH_SHORT).show();
//        }
        else {
            getData(name.getText().toString(),classes.getText().toString(),address.getText().toString(),password.getText().toString());
        }

    }

    private void getData(String Name, String Classes, String Address, String Password) {

        animationView.setVisibility(View.VISIBLE);

        Map<String, String> params = new Hashtable<String, String>();
        params.put("name",Name);
        params.put("email_id",sharedPreferences.getString("email","null"));
        params.put("class",Classes);
        params.put("address",Address);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_update_user, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String message = response.getString("message");
                    String code = response.getString("error_code");

                    if (code.equals("200")){

                        Toasty.success(Update.this, "Successfully Updated", Toast.LENGTH_SHORT, true).show();

                        SharedPreferences.Editor editors = sharedPreferences.edit();
                        editors.putString("address", Address);
                        editors.putString("name", Name);
                        editors.putString("class", Classes);
                        editors.apply();

                        Intent intent = new Intent(Update.this,Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                    else {
                        Toasty.error(Update.this, message, Toast.LENGTH_SHORT, true).show();
                        animationView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    animationView.setVisibility(View.GONE);
                    Toasty.error(Update.this, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                animationView.setVisibility(View.GONE);
                Toasty.error(Update.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Update.this);
        queue.add(jsonRequest);

    }
}