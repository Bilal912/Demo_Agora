package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu_touch.edu_hunt.volley.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Change_password extends AppCompatActivity {
    EditText editText;
    LottieAnimationView animationView;
    SharedPreferences sharedPreferences;
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        imageView = findViewById(R.id.iv_header_img);

        animationView = (LottieAnimationView) findViewById(R.id.anime);

        editText = findViewById(R.id.femail);
        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String pic = sharedPreferences.getString("picture","null");
        Glide.with(Change_password.this)
                .load(pic)
                .centerCrop()
                .placeholder(R.drawable.user2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);

    }

    public void back(View view) {
        finish();
    }

    public void change_password(View view) {
        if (TextUtils.isEmpty(editText.getText().toString())){
            makeText(Change_password.this, "Field is required", LENGTH_SHORT).show();
        }
        else {
            getData(editText.getText().toString());
        }

    }

    private void getData(String pass) {
        animationView.setVisibility(View.VISIBLE);
        Map<String, String> params = new Hashtable<String, String>();
        params.put("email",sharedPreferences.getString("email","null"));
        params.put("new_pass",pass);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_changepassword, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String message = response.getString("message");
                    String code = response.getString("error code");

                    if (code.equals("200")){
                        Toasty.success(Change_password.this, message, Toast.LENGTH_SHORT, true).show();
                        finish();
                    }
                    else {
                        Toasty.error(Change_password.this, message, Toast.LENGTH_SHORT, true).show();
                        animationView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    animationView.setVisibility(View.GONE);
                    Toasty.error(Change_password.this, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                animationView.setVisibility(View.GONE);
                Toasty.error(Change_password.this, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(Change_password.this);
        queue.add(jsonRequest);

    }
}