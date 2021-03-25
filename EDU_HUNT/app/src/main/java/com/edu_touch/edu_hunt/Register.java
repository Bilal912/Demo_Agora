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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edu_touch.edu_hunt.Adapter.Subject_Adapter;
import com.edu_touch.edu_hunt.Model.subject_model;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Register extends AppCompatActivity {
EditText name,phone,email,address,classes,password,confirm_password,city,state,zip;
Spinner spinner,spinner_classgroup,spinner_board,spinner_subject;
CircleImageView imageView;
CheckBox checkBox;
LinearLayout linear_class,linear_subject;
public static Bitmap bitmap = null;
    Uri uri;
    public static final int RESULT_LOAD_IMAGE = 1;
    android.app.AlertDialog loadings;

    TextView terms;
    SharedPreferences sharedPreferences;
    ArrayList<String> clasy,class_group,class_boards,subjects;
    ArrayList<String> clasy_id,class_group_id,class_boards_id,subjects_id;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        terms = findViewById(R.id.terms);

        linear_class = findViewById(R.id.linear_class);
        linear_subject = findViewById(R.id.linear_subject);

        clasy_id = new ArrayList<>();
        class_boards_id = new ArrayList<>();
        subjects_id = new ArrayList<>();
        class_group_id = new ArrayList<>();

        loadings = new ProgressDialog(Register.this);

        spinner_subject = findViewById(R.id.spinner_subject);

        subjects = new ArrayList<>();
        imageView = findViewById(R.id.image_id);
        clasy = new ArrayList<>();
        class_boards = new ArrayList<>();
        class_group = new ArrayList<>();

        spinner_board = findViewById(R.id.spinner_boards);
        spinner_classgroup = findViewById(R.id.spinner_classgroup);

        class_group.add("Select Class Groups");
        class_boards.add("Select Boards");
        clasy.add("Select Class");
        subjects.add("Select Subject");


        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        zip = findViewById(R.id.zip);
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

        loadings.setMessage("Please Wait a Moment...");
        loadings.show();

        getBoard();
        getClasses();


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Terms_and_conditions.class));
            }
        });

//        spinner_board.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (spinner_board.getSelectedItem().toString().equals("Select Boards")){
//                    linear_class.setVisibility(View.GONE);
//                    linear_subject.setVisibility(View.GONE);
//                }else {
//                    linear_class.setVisibility(View.VISIBLE);
//                    linear_subject.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (spinner.getSelectedItem().toString().equals("Select Class")) {
//                    linear_subject.setVisibility(View.GONE);
//                }else {
//                    getSubject();
//                    linear_subject.setVisibility(View.VISIBLE);
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        //getSubject();
//        getClassGroup();

//        loading.dismiss();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
                catch (Exception e){
                    e.printStackTrace();
                    makeText(Register.this, "Can't Open Your Media", LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getBoard() {

        final android.app.AlertDialog loading = new ProgressDialog(Register.this);
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
                            class_boards_id.add(object.getString("id"));
                        }
                        spinner_board.setAdapter(new ArrayAdapter<String>(Register.this,
                                android.R.layout.simple_dropdown_item_1line,
                                class_boards));
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

    private void getClassGroup() {

        final android.app.AlertDialog loading = new ProgressDialog(Register.this);
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
                            class_group_id.add(object.getString("id"));
                        }

                        spinner_classgroup.setAdapter(new ArrayAdapter<String>(Register.this,
                                android.R.layout.simple_dropdown_item_1line,
                                class_group));
                        loadings.dismiss();
                    }
                    else {
                        loadings.dismiss();
                    }

                } catch (JSONException e) {
                    loadings.dismiss();
                    Toast.makeText(Register.this,"Internet Issue", LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadings.dismiss();
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

    private void getClasses() {

        final android.app.AlertDialog loading = new ProgressDialog(Register.this);
        loading.setMessage("Please Wait a Moment...");
//        loading.show();

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
                            clasy_id.add(object.getString("id"));
                        }

                        spinner.setAdapter(new ArrayAdapter<String>(Register.this,
                                android.R.layout.simple_dropdown_item_1line,
                                clasy));
                        loadings.dismiss();
                    }
                    else {
                        loadings.dismiss();
                    }

                } catch (JSONException e) {
                    loadings.dismiss();
                    Toast.makeText(Register.this,"Internet Issue", LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadings.dismiss();
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

        else if (spinner_board.getSelectedItem().toString().equals("Select Boards")){
            Toast.makeText(Register.this,"Select Your Board",
                    Toast.LENGTH_LONG).show();
        }
        else if (spinner.getSelectedItem().toString().equals("Select Class")){
            Toast.makeText(Register.this,"Select Your Class",
                    Toast.LENGTH_LONG).show();
        }
//        else if (spinner_subject.getSelectedItem().toString().equals("Select Subject")){
//            makeText(Register.this, "Subject is required", LENGTH_SHORT).show();
//        }
//        else if (spinner_classgroup.getSelectedItem().toString().equals("Select Class Groups")){
//            Toast.makeText(Register.this,"Select Your Class Group",
//                    Toast.LENGTH_LONG).show();
//        }
        else if (TextUtils.isEmpty(city.getText().toString())){
            makeText(Register.this, "City is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(state.getText().toString())){
            makeText(Register.this, "State is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(zip.getText().toString())){
            makeText(Register.this, "ZIP is required", LENGTH_SHORT).show();
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
        else if (bitmap == null){
            makeText(Register.this, "Picture is required", LENGTH_SHORT).show();
        }
        else {
            if (isEmailValid(email.getText().toString().trim())){
                if (password.getText().toString().trim().equals(confirm_password.getText().toString().trim())){
                    if (checkBox.isChecked()){
//                    getData();

                        Intent i = new Intent(Register.this,Phoneno.class);
                        i.putExtra("name",name.getText().toString().trim());
                        i.putExtra("email_id",email.getText().toString().trim());
                        i.putExtra("password",password.getText().toString().trim());

                        i.putExtra("class", clasy_id.get(spinner.getSelectedItemPosition()-1));
                        //i.putExtra("class_group", class_group_id.get(spinner_classgroup.getSelectedItemPosition()-1));
                        i.putExtra("board", class_boards_id.get(spinner_board.getSelectedItemPosition()-1));
//                        i.putExtra("subject", subjects_id.get(spinner_subject.getSelectedItemPosition()-1));

                        i.putExtra("address",address.getText().toString().trim());
                        i.putExtra("zip",zip.getText().toString().trim());
                        i.putExtra("city",city.getText().toString().trim());
                        i.putExtra("state",state.getText().toString().trim());
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
            else {
                makeText(Register.this, "Email is not Valid", LENGTH_SHORT).show();
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
                    makeText(Register.this,"Error Loading Image", LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getSubject() {

        loadings.show();
        Map<String, String> params = new Hashtable<String, String>();
        params.put("class_group",clasy_id.get(spinner.getSelectedItemPosition()-1));
        params.put("board",class_boards_id.get(spinner_board.getSelectedItemPosition()-1));

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_getsubjectbtclassandboard, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");
                    String code = response.getString("error code");

                    if (code.equals("200")){

                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            subject_model s = new subject_model();
                            String id = object.getString("id");
                            subjects.add(object.getString("title"));
                            subjects_id.add(id);
                        }
                        spinner_subject.setAdapter(new ArrayAdapter<String>(Register.this,
                                android.R.layout.simple_dropdown_item_1line,
                                subjects));
                        loadings.dismiss();

                    }
                    else {
                        loadings.dismiss();
                        //animationView.setVisibility(View.GONE);
                        Toasty.error(Register.this, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                    loadings.dismiss();
                    //  loading.dismiss();
                    e.printStackTrace();
                    Toasty.error(Register.this, "No Data Found", Toast.LENGTH_SHORT, true).show();

                    //animationView.setVisibility(View.GONE);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                //animationView.setVisibility(View.GONE);
                loadings.dismiss();
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

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}