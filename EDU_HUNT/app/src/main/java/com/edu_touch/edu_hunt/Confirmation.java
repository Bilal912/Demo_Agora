package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.edu_touch.edu_hunt.Adapter.My_Teacher_Adapter;
import com.edu_touch.edu_hunt.Model.payment_history_model;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class Confirmation extends AppCompatActivity {
    private PinView pinView;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        pinView=findViewById(R.id.pinview);

        id = getIntent().getStringExtra("id");

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

//                String codee=pinView.getText().toString();
//                if(pinView.getText().toString().length()==4){
//                    if(codee.equals(code)){
//                        makeRegisterRequest(name,number,email,pass,address,classes);
//                    }else{
//                        final SweetAlertDialog loading=new SweetAlertDialog(otp.this,SweetAlertDialog.ERROR_TYPE);
//                        loading.setTitleText("Invalid Code");
//                        loading.show();
//                    }
//                }
            }
        });


    }

    public void back(View view) {
        finish();
    }

    public void Confirmy(View view) {

//        if(pinView.getText().toString().length()==4){
            getData(pinView.getText().toString(),id);
//        }
//        else {
//            Toast.makeText(this, "Enter 4 digit OTP", Toast.LENGTH_SHORT).show();
//        }

    }

    private void getData(String otp, String id) {
        final AlertDialog loading = new ProgressDialog(Confirmation.this);
        loading.setMessage("Please Wait....");
        loading.setCancelable(false);
        loading.show();


        Map<String, String> params = new Hashtable<String, String>();
        params.put("teacher_otp",otp);
        params.put("booking_id",id);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_submiting_booking_otp, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error_code");

                    if (code.equals("200")){
                        Toasty.success(Confirmation.this, message, Toast.LENGTH_SHORT, true).show();
                        finish();
                        My_teacher.loader = 1;
                        loading.dismiss();
                    }
                    else {

                        loading.dismiss();
                        Toasty.error(Confirmation.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    loading.dismiss();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading.dismiss();
                Toasty.error(Confirmation.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Confirmation.this);
        queue.add(jsonRequest);

    }
}