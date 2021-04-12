package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.edu_touch.edu_hunt.Adapter.Book_Now_Adapter;
import com.edu_touch.edu_hunt.Adapter.Fee_Adapter;
import com.edu_touch.edu_hunt.Model.fee_model;
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

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Payment extends AppCompatActivity {
    WebView webView;
    String amount,ID,check,fee_id,booking_id,class_id,subject_id,board_id,feess;
    SharedPreferences sharedPreferences;

    String transaction_id;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        this.webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        ID = sharedPreferences.getString("id", "Null");
        check = getIntent().getStringExtra("fee");
        if (check.equals("Fee Deposit")){
            amount = getIntent().getStringExtra("amount");
            fee_id = getIntent().getStringExtra("fee_id");
            booking_id = getIntent().getStringExtra("booking_id");
        }
        else {
            amount = check;

            subject_id = getIntent().getStringExtra("subjects_id");
            class_id = getIntent().getStringExtra("class_id");
            board_id = getIntent().getStringExtra("boards_id");
            feess = getIntent().getStringExtra("feess");

        }

        webView.loadUrl(Constant.Base_url_payment+"amount="+amount+"&user-id="+ID);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //   checkConnection();
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {

                //view.loadUrl(webView.getUrl());

                String Url = view.getUrl();
                AdWebViewClient(Url);

            }

        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress>=60)
                {

                }

            }
        });

    }

    public void AdWebViewClient(String url) {

        //view.loadUrl(webView.getUrl());

        if (url.contains("success")) {

            Uri uri= Uri.parse(webView.getUrl());
            transaction_id = uri.getQueryParameter("id");

            if (check.equals("Fee Deposit")){
                Submittingfee();
            }
            else {

                Book_Now_Adapter.clicky.onclicky(subject_id, class_id
                        , board_id, feess, transaction_id);

                //Toast.makeText(this, "Now You can book your Teacher", Toast.LENGTH_LONG).show();
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("status", "1");
//                editor.apply();

                finish();
            }


//Uri parse
//            Uri uri= Uri.parse(webView.getUrl());
//            transaction_id = uri.getQueryParameter("txtid");
//            getData(transaction_id);

        }
        else if (url.contains("failure")){

            Toast.makeText(Payment.this, "Payment was not received", Toast.LENGTH_LONG).show();

            webView.destroy();
            final SweetAlertDialog pDialog = new SweetAlertDialog(Payment.this, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Error");
            pDialog.setContentText("Payment was not received");
            pDialog.setConfirmText("OK");
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Intent i = new Intent(Payment.this,Home.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            });

            finish();

        }

    }

    private void Submittingfee() {

        final AlertDialog loading = new ProgressDialog(Payment.this);
        loading.setMessage("Please Wait....");
        loading.setCancelable(false);
        loading.show();

        Map<String, String> params = new Hashtable<String, String>();
        params.put("booking_id",booking_id);
        params.put("fees_id",fee_id);
        params.put("billdesk_id",transaction_id);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_submiting_fee, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error_code");

                    if (code.equals("200")){
                        Toasty.success(Payment.this, message, Toast.LENGTH_SHORT, true).show();
                        finish();
                        loading.dismiss();
                        Fees.loady = 1;

                    }
                    else {

                        loading.dismiss();
                        //Toasty.error(Payment.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {

                    loading.dismiss();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading.dismiss();
                Toasty.error(Payment.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Payment.this);
        queue.add(jsonRequest);


    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();

        checkConnection();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }

    public void checkConnection() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                webView.setVisibility(View.VISIBLE);

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                webView.setVisibility(View.VISIBLE);

            }
        } else {
            webView.setVisibility(View.GONE);
        }
    }

}
