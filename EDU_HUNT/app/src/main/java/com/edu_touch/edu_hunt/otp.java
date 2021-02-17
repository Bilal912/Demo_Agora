package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.edu_touch.edu_hunt.volley.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class otp extends AppCompatActivity {

    private PinView pinView;
    String code;
    String name,email,pass,classes,address,number;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        animationView = findViewById(R.id.anime);
        pinView=findViewById(R.id.pinview);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email_id");
        pass = getIntent().getStringExtra("password");
        address = getIntent().getStringExtra("address");
        classes = getIntent().getStringExtra("class");

        code = getIntent().getStringExtra("code");

        Toast.makeText(otp.this,code,Toast.LENGTH_LONG).show();

        number = getIntent().getStringExtra("number");

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
                        makeRegisterRequest(name,number,email,pass,address,classes);
                    }else{
                        final SweetAlertDialog loading=new SweetAlertDialog(otp.this,SweetAlertDialog.ERROR_TYPE);
                        loading.setTitleText("Invalid Code");
                        loading.show();
                    }
                }
            }
        });


    }

    private void makeRegisterRequest(String name, String number, String email, String pass, String address, String classes) {

        animationView.setVisibility(View.VISIBLE);

        Map<String, String> params = new Hashtable<String, String>();
        params.put("name",name);
        params.put("contact_no",number);
        params.put("email_id",email);
        params.put("password",pass);
        params.put("class",classes);
        params.put("address",address);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_signup, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String message = response.getString("message");
                    String code = response.getString("error_code");

                    if (code.equals("200")){

                        Toasty.success(otp.this, message, Toast.LENGTH_SHORT, true).show();

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

    public void otpy(View view) {
        if(pinView.getText().toString().equals(code)){
            makeRegisterRequest(name,number,email,pass,address,classes);
        }else{
            final SweetAlertDialog loading=new SweetAlertDialog(otp.this,SweetAlertDialog.ERROR_TYPE);
            loading.setTitleText("Invalid Code");
            loading.show();
        }
    }
}