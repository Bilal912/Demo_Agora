package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.edu_touch.edu_hunt.volley.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class otp extends AppCompatActivity {

    private PinView pinView;
    String code;
    String name,email,pass,classes,address,number,city,state,zip,board,class_group,subject;
    LottieAnimationView animationView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        board = getIntent().getStringExtra("board");
        class_group = getIntent().getStringExtra("class_group");

        //subject = getIntent().getStringExtra("subject");
        animationView = findViewById(R.id.anime);
        pinView=findViewById(R.id.pinview);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email_id");
        pass = getIntent().getStringExtra("password");
        address = getIntent().getStringExtra("address");
        classes = getIntent().getStringExtra("class");

        code = getIntent().getStringExtra("code");

//        Toast.makeText(otp.this,code,Toast.LENGTH_LONG).show();

        number = getIntent().getStringExtra("number");
        zip = getIntent().getStringExtra("zip");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String codee=pinView.getText().toString();
                if(pinView.getText().toString().length()==4){
                    if(codee.equals(code)){
//                        makeRegisterRequest(name,number,email,pass,address,classes);
                        getData();
                    }else{
                        final SweetAlertDialog loading=new SweetAlertDialog(otp.this,SweetAlertDialog.ERROR_TYPE);
                        loading.setTitleText("Invalid Code");
                        loading.show();
                    }
                }
            }
        });

    }

//    private void makeRegisterRequest(String name, String number, String email, String pass, String address, String classes) {
//
//        animationView.setVisibility(View.VISIBLE);
//
//        Map<String, String> params = new Hashtable<String, String>();
//        params.put("name",name);
//        params.put("contact_no",number);
//        params.put("email_id",email);
//        params.put("password",pass);
//        params.put("class",classes);
//        params.put("address",address);
//
//        params.put("board",board);
//        params.put("class_group",class_group);
//
//        params.put("city",city);
//        params.put("zip",zip);
//        params.put("state",state);
//
//        params.put("lang",sharedPreferences.getString("lang","0"));
//        params.put("lat",sharedPreferences.getString("lat","0"));
//
//        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_signup, params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    String message = response.getString("message");
//                    String code = response.getString("error_code");
//
//                    if (code.equals("200")){
//
//                        Toasty.success(otp.this, message, Toast.LENGTH_SHORT, true).show();
//
//                        Intent intent = new Intent(otp.this,MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    }
//                    else {
//                        Toasty.error(otp.this, message, Toast.LENGTH_SHORT, true).show();
//                        animationView.setVisibility(View.GONE);
//                    }
//
//                } catch (JSONException e) {
//                    //  loading.dismiss();
//                    e.printStackTrace();
//                    animationView.setVisibility(View.GONE);
//                    Toasty.error(otp.this, "Error", Toast.LENGTH_SHORT, true).show();
//                }
//            }
//        }
//                , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //loading.dismiss();
//                animationView.setVisibility(View.GONE);
//                Toasty.error(otp.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
//            }
//        });
//        jsonRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//        RequestQueue queue = Volley.newRequestQueue(otp.this);
//        queue.add(jsonRequest);
//
//    }

    public void getData(){

        animationView.setVisibility(View.VISIBLE);

        final AlertDialog loading = new ProgressDialog(otp.this);
        loading.setMessage("Updating...");
        //loading.setCancelable(false);
//        loading.show();

        VolleyMultipartRequest jsonRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.Base_url_signup, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {
                    JSONObject obj = new JSONObject(new String(response.data));

                    String message = obj.getString("message");
                    String code = obj.getString("error_code");

                    if (code.equals("200")){

                        Toasty.success(otp.this, message, Toast.LENGTH_SHORT, true).show();
                        Register.bitmap = null;
                        Intent intent = new Intent(otp.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                    else {
                        Toasty.error(otp.this, message, Toast.LENGTH_SHORT, true).show();
                        animationView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    //  loading.dismiss();
                    e.printStackTrace();
                    animationView.setVisibility(View.GONE);
//                    Toasty.error(otp.this, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                animationView.setVisibility(View.GONE);
                loading.dismiss();
                makeText(otp.this, "Connection Timed Out", LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("name",name);
                params.put("contact_no",number);
                params.put("email_id",email);
                params.put("password",pass);
                params.put("class",classes);
                params.put("address",address);
                params.put("board",board);
                //params.put("subject",subject);
//                params.put("class_group",class_group);

                params.put("city",city);
                params.put("zip",zip);
                params.put("state",state);

                params.put("lang",sharedPreferences.getString("lang","0"));
                params.put("lat",sharedPreferences.getString("lat","0"));

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long ima = System.currentTimeMillis();
                String imagename = "IMG"+String.valueOf(ima).concat(".jpg");
                params.put("profile_image",  new DataPart(Constant.image_url+imagename, getFileDataFromDrawable(Register.bitmap)));
                return params;
            }
        };
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
        Volley.newRequestQueue(this).add(jsonRequest);

    }

    public byte[] getFileDataFromDrawable(Bitmap b) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void otpy(View view) {
        if(pinView.getText().toString().equals(code)){
//            makeRegisterRequest(name,number,email,pass,address,classes);
            getData();
        }else{
            final SweetAlertDialog loading=new SweetAlertDialog(otp.this,SweetAlertDialog.ERROR_TYPE);
            loading.setTitleText("Invalid Code");
            loading.show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Phoneno.animationView.setVisibility(View.GONE);
    }


    private void resendotp(String number) {

        animationView.setVisibility(View.VISIBLE);
        Map<String, String> params = new Hashtable<String, String>();
        params.put("number", number);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_sendotp, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error_code");

                    if (code.equals("200")) {

                        Toasty.success(otp.this, message, Toast.LENGTH_SHORT, true).show();

                        animationView.setVisibility(View.VISIBLE);
//                        int j = response.getInt("code");
//                        Intent i = new Intent(Phoneno.this, otp.class);
//                        i.putExtra("name", name);
//                        i.putExtra("board", board);
//                        //i.putExtra("class_group",class_group);
//                        //i.putExtra("subject",subject);
//
//                        i.putExtra("email_id", email);
//                        i.putExtra("password", pass);
//                        i.putExtra("class", classes);
//                        i.putExtra("address", address);
//                        i.putExtra("zip", zip);
//                        i.putExtra("city", city);
//                        i.putExtra("state", state);
//
//                        i.putExtra("number", "+91".concat(number));
//                        i.putExtra("code", String.valueOf(j));
//                        startActivity(i);
                    } else {
                        Toasty.error(otp.this, message, Toast.LENGTH_SHORT, true).show();
                        animationView.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    //  loading.dismiss();
                    e.printStackTrace();
                    animationView.setVisibility(View.GONE);
                    Toasty.error(otp.this, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                animationView.setVisibility(View.GONE);
                Toasty.error(otp.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(otp.this);
        queue.add(jsonRequest);
    }

    public void otp_resend(View view) {
        resendotp(number);
    }
}