package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edu_touch.edu_hunt.volley.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Register extends AppCompatActivity {
EditText name,phone,email,address,classes,password,confirm_password;
Spinner spinner;
CheckBox checkBox;
SharedPreferences sharedPreferences;
    ArrayList<String> clasy;

    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        clasy = new ArrayList<>();
        clasy.add("Class");

        spinner = findViewById(R.id.spinner_id);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
//        animationView = (LottieAnimationView) findViewById(R.id.anime);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.number);
        address = (EditText) findViewById(R.id.address);
//        classes = (EditText) findViewById(R.id.classes);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);

        getClasses();

    }

    private void getClasses() {

        final android.app.AlertDialog loading = new ProgressDialog(Register.this);
        loading.setMessage("Please Wait a Moment...");
        loading.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_getclasses
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String code = response.getString("error code");

                    if (code.equals("200")) {

                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            clasy.add(object.getString("name"));
                        }

                        spinner.setAdapter(new ArrayAdapter<String>(Register.this,
                                android.R.layout.simple_dropdown_item_1line,
                                clasy));
                        loading.dismiss();
                    }
                    else {
                        loading.dismiss();
                    }

                } catch (JSONException e) {
                    loading.dismiss();
                    Toast.makeText(Register.this,"Internet Issue", LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Register.this, "Connection Timed Out" ,Toast.LENGTH_LONG).show();
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

    public void submit(View view) {

        if (TextUtils.isEmpty(name.getText().toString())){
            makeText(Register.this, "Name is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email.getText().toString())){
            makeText(Register.this, "Email is required", LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(phone.getText().toString())){
//            makeText(Register.this, "Contact Number is required", LENGTH_SHORT).show();
//        }

//        else if (TextUtils.isEmpty(classes.getText().toString())){
//            makeText(Register.this, "Class is required", LENGTH_SHORT).show();
//        }

        else if (spinner.getSelectedItem().toString().equals("Class")){
            Toast.makeText(Register.this,"Select Your Class",
                    Toast.LENGTH_LONG).show();
        }

        else if (TextUtils.isEmpty(address.getText().toString())){
            makeText(Register.this, "Address is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password.getText().toString())){
            makeText(Register.this, "Password is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirm_password.getText().toString())){
            makeText(Register.this, "Confirm Password is required", LENGTH_SHORT).show();
        }
        else {
            if (password.getText().toString().trim().equals(confirm_password.getText().toString().trim())){
                if (checkBox.isChecked()){
//                    getData();

                    Intent i = new Intent(Register.this,Phoneno.class);
                    i.putExtra("name",name.getText().toString().trim());
                    i.putExtra("email_id",email.getText().toString().trim());
                    i.putExtra("password",password.getText().toString().trim());
                    i.putExtra("class",spinner.getSelectedItem().toString().trim());
                    i.putExtra("address",address.getText().toString().trim());
                    startActivity(i);

                }
                else {
                    makeText(Register.this, "Checkbox is unchecked", LENGTH_SHORT).show();
                }
            }
            else {
                makeText(Register.this, "Password not match", LENGTH_SHORT).show();
            }

        }

    }

    public void getData() {
        final AlertDialog loading = new ProgressDialog(Register.this);
        loading.setMessage("Please Wait...");
        loading.setCancelable(false);
        //loading.show();
//        animationView.setVisibility(View.VISIBLE);

        Map<String, String> params = new Hashtable<String, String>();
        params.put("name",name.getText().toString().trim());
        params.put("contact_no",phone.getText().toString().trim());
        params.put("email_id",email.getText().toString().trim());
        params.put("password",password.getText().toString().trim());
        params.put("class",classes.getText().toString().trim());
        params.put("address",address.getText().toString().trim());

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_signup, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String message = response.getString("message");
                    String code = response.getString("error_code");

                    if (code.equals("200")){

                        Toasty.success(Register.this, message, Toast.LENGTH_SHORT, true).show();

                        Intent intent = new Intent(Register.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toasty.error(Register.this, message, Toast.LENGTH_SHORT, true).show();
                    }

//                    animationView.setVisibility(View.GONE);
                } catch (JSONException e) {
                  //  loading.dismiss();
                    e.printStackTrace();
//                    animationView.setVisibility(View.GONE);
                    Toasty.error(Register.this, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
//                animationView.setVisibility(View.GONE);
                Toasty.error(Register.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        queue.add(jsonRequest);

    }

    public void back(View view) {
        finish();
    }

    public void Login(View view) {
        startActivity(new Intent(Register.this,Login.class));
    }
}