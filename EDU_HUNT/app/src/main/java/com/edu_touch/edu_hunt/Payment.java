package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Payment extends AppCompatActivity {
    WebView webView;
    String amount,ID;
    SharedPreferences sharedPreferences;

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
        amount = getIntent().getStringExtra("fee");

        webView.loadUrl(Constant.Base_url_payment+"amount="+amount+"20&user-id="+ID);

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

            Toast.makeText(this, "Now You can book your Teacher", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("status", "1");
            editor.apply();

//Uri parse
//            Uri uri= Uri.parse(webView.getUrl());
//            transaction_id = uri.getQueryParameter("txtid");
//            getData(transaction_id);
            finish();
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