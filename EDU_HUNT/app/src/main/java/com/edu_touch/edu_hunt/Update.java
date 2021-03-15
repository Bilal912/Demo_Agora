package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.edu_touch.edu_hunt.volley.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;
import static com.edu_touch.edu_hunt.Register.RESULT_LOAD_IMAGE;

public class Update extends AppCompatActivity {
    EditText name,phone,email,address,classes,password,city,state,zip;
    SharedPreferences sharedPreferences;
    Spinner spinner,spinner_classgroup,spinner_board;
    LottieAnimationView animationView;
    CircleImageView imageView;
    Bitmap bitmap;
    Uri uri;
    String imagename;
    ArrayList<String> clasy,class_group,class_boards,class_board_id,clasy_id;

    android.app.AlertDialog loadings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        clasy_id = new ArrayList<>();
        class_board_id = new ArrayList<>();

        loadings = new ProgressDialog(Update.this);
        loadings.setMessage("Please Wait a Moment...");
        loadings.show();


        class_boards = new ArrayList<>();
        class_group = new ArrayList<>();

        spinner_board = findViewById(R.id.spinner_boards);
        spinner_classgroup = findViewById(R.id.spinner_classgroup);
        spinner = findViewById(R.id.spinner_id);
        clasy = new ArrayList<>();
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        zip = findViewById(R.id.zip);

        imageView = findViewById(R.id.image_id);
        animationView = (LottieAnimationView) findViewById(R.id.anime);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.number);
        address = (EditText) findViewById(R.id.address);
        //classes = (EditText) findViewById(R.id.classes);
        password = (EditText) findViewById(R.id.password);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name","null"));
        phone.setText(sharedPreferences.getString("phone","null"));
        email.setText(sharedPreferences.getString("email","null"));
        address.setText(sharedPreferences.getString("address","null"));
        //classes.setText(sharedPreferences.getString("class","null"));
        password.setText(sharedPreferences.getString("password","null"));

        city.setText(sharedPreferences.getString("user_city","null"));
        zip.setText(sharedPreferences.getString("zip","null"));
        state.setText(sharedPreferences.getString("state","null"));

        String pic = sharedPreferences.getString("picture","null");

        //Picasso.get().load(pic).into(imageView);

        Glide.with(Update.this)
                .load(pic)
                .centerCrop()
                .placeholder(R.drawable.user2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);

        getBoard();
        getClasses();
        //getClassGroup();




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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, Register.RESULT_LOAD_IMAGE);
                }
                catch (Exception e){
                    e.printStackTrace();
                    makeText(Update.this, "Can't Open Your Media", LENGTH_SHORT).show();
                }

            }
        });

    }
    private void getBoard() {

        final android.app.AlertDialog loading = new ProgressDialog(Update.this);
        loading.setMessage("Please Wait a Moment...");
//        loading.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_getboard
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String code = response.getString("error code");
                    if (code.equals("200")) {

                        JSONArray jsonArray = response.getJSONArray("Boards");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            class_boards.add(object.getString("name"));
                            class_board_id.add(object.getString("id"));
                        }
                        spinner_board.setAdapter(new ArrayAdapter<String>(Update.this,
                                android.R.layout.simple_dropdown_item_1line,
                                class_boards));

                        for (int k = 0; k < class_boards.size(); k++) {
                            if (sharedPreferences.getString("board","null").equals(class_board_id.get(k))){
                                spinner_board.setSelection(k);
                            }
                        }

                    }
                    else {
                    }
                } catch (JSONException e) {
                    Toast.makeText(Update.this,"Internet Issue", LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Update.this, "Connection Timed Out" ,Toast.LENGTH_LONG).show();
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

    private void getClassGroup() {

        final android.app.AlertDialog loading = new ProgressDialog(Update.this);
        loading.setMessage("Please Wait a Moment...");
//        loading.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_getclass_group
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String code = response.getString("error code");

                    if (code.equals("200")) {

                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            class_group.add(object.getString("group_name"));
                        }

                        spinner_classgroup.setAdapter(new ArrayAdapter<String>(Update.this,
                                android.R.layout.simple_dropdown_item_1line,
                                class_group));

                        for (int k = 0; k < class_group.size(); k++) {
                            if (sharedPreferences.getString("class_group","null").equals(class_group.get(k))){
                                spinner_classgroup.setSelection(k);
                            }
                        }

                        loading.dismiss();
                    }
                    else {
                        loading.dismiss();
                    }

                } catch (JSONException e) {
                    loading.dismiss();
                    Toast.makeText(Update.this,"Internet Issue", LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Update.this, "Connection Timed Out" ,Toast.LENGTH_LONG).show();
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
//        else if (TextUtils.isEmpty(classes.getText().toString())){
//            makeText(Update.this, "Class is required", LENGTH_SHORT).show();
//        }
        else if (TextUtils.isEmpty(address.getText().toString())){
            makeText(Update.this, "Address is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(city.getText().toString())){
            makeText(Update.this, "City is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(state.getText().toString())){
            makeText(Update.this, "State is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(zip.getText().toString())){
            makeText(Update.this, "ZIP is required", LENGTH_SHORT).show();
        }

//        else if (TextUtils.isEmpty(password.getText().toString())){
//            makeText(Update.this, "Password is required", LENGTH_SHORT).show();
//        }
        else {

            if (bitmap == null) {
                getData(name.getText().toString(), address.getText().toString(),
                        password.getText().toString(), city.getText().toString(), state.getText().toString(),
                        zip.getText().toString());
            } else {
                uploadbitmap(name.getText().toString(), address.getText().toString(),
                        password.getText().toString(), city.getText().toString(), state.getText().toString(),
                        zip.getText().toString());

            }
        }

    }

    private void uploadbitmap(String Name, String Address, String Password,String City
            ,String State,String ZIP) {

        animationView.setVisibility(View.VISIBLE);

        final AlertDialog loading = new ProgressDialog(Update.this);
        loading.setMessage("Updating...");
        //loading.setCancelable(false);
//        loading.show();

        VolleyMultipartRequest jsonRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.Base_url_update_user, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {
                    JSONObject obj = new JSONObject(new String(response.data));

                    String message = obj.getString("message");
                    String code = obj.getString("error_code");

                    if (code.equals("200")){

                        Toasty.success(Update.this, "Successfully Updated", Toast.LENGTH_SHORT, true).show();

                        SharedPreferences.Editor editors = sharedPreferences.edit();
                        editors.putString("address", Address);
                        editors.putString("name", Name);
                        editors.putString("class", clasy_id.get(spinner.getSelectedItemPosition()));
                        editors.putString("board", class_board_id.get(spinner_board.getSelectedItemPosition()));
                        //editors.putString("class_group", spinner_classgroup.getSelectedItem().toString().trim());

                        editors.putString("user_city", City);
                        editors.putString("state", State);
                        editors.putString("zip", ZIP);
                        editors.putString("picture",Constant.image_url+imagename);
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
                loading.dismiss();
                makeText(Update.this, "Connection Timed Out", LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("name",Name);
                params.put("email_id",sharedPreferences.getString("email","null"));
                params.put("address",Address);


                params.put("class", clasy_id.get(spinner.getSelectedItemPosition()));
                params.put("board", class_board_id.get(spinner_board.getSelectedItemPosition()));
                //params.put("class_group",spinner_classgroup.getSelectedItem().toString().trim());

                params.put("city",City);
                params.put("state",State);
                params.put("zip",ZIP);

//                params.put("lang",sharedPreferences.getString("lang","0"));
//                params.put("lat",sharedPreferences.getString("lat","0"));

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long ima = System.currentTimeMillis();
                imagename = "IMG"+String.valueOf(ima).concat(".jpg");
                params.put("profile_image",  new DataPart(Constant.image_url+imagename, getFileDataFromDrawable(bitmap)));
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

    private void getData(String Name, String Address, String Password,String City
    ,String State,String ZIP) {

        animationView.setVisibility(View.VISIBLE);

        Map<String, String> params = new Hashtable<String, String>();
        params.put("name",Name);
        params.put("email_id",sharedPreferences.getString("email","null"));

        params.put("class", clasy_id.get(spinner.getSelectedItemPosition()));
        params.put("board", class_board_id.get(spinner_board.getSelectedItemPosition()));
        //params.put("class_group",spinner_classgroup.getSelectedItem().toString().trim());
        params.put("address",Address);

        params.put("city",City);
        params.put("state",State);
        params.put("zip",ZIP);

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
                        editors.putString("user_city", City);
                        editors.putString("state", State);
                        editors.putString("zip", ZIP);

                        editors.putString("class", clasy_id.get(spinner.getSelectedItemPosition()));
                        editors.putString("board", class_board_id.get(spinner_board.getSelectedItemPosition()));
                        //editors.putString("class_group", spinner_classgroup.getSelectedItem().toString().trim());

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

    public void back(View view) {
        finish();
    }

    public byte[] getFileDataFromDrawable(Bitmap b) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                Picasso.get().load(data.getData()).noPlaceholder()
                        .into(imageView);
                uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    makeText(Update.this,"Error Loading Image", LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getClasses() {

        final android.app.AlertDialog loading = new ProgressDialog(Update.this);
        loading.setMessage("Please Wait a Moment...");
        loading.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_getclasses
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String code = response.getString("error code");
                    String current_class = sharedPreferences.getString("class","null");

                    if (code.equals("200")) {

                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            clasy.add(object.getString("name"));
                            clasy_id.add(object.getString("id"));
                        }

                        spinner.setAdapter(new ArrayAdapter<String>(Update.this,
                                android.R.layout.simple_dropdown_item_1line,
                                clasy));

                        for (int k = 0; k < clasy.size(); k++) {
                            if (current_class.equals(clasy_id.get(k))){
                                spinner.setSelection(k);
                            }
                        }

                        loadings.dismiss();
                        loading.dismiss();
                    }
                    else {
                        loadings.dismiss();

                        loading.dismiss();
                    }

                } catch (JSONException e) {
                    loadings.dismiss();

                    loading.dismiss();
                    Toast.makeText(Update.this,"Internet Issue", LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadings.dismiss();

                loading.dismiss();
                Toast.makeText(Update.this, "Connection Timed Out" ,Toast.LENGTH_LONG).show();
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

}