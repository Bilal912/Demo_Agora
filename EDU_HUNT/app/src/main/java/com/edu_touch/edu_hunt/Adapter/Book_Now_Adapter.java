package com.edu_touch.edu_hunt.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.edu_touch.edu_hunt.Constant;
import com.edu_touch.edu_hunt.Home;
import com.edu_touch.edu_hunt.Login;
import com.edu_touch.edu_hunt.Model.select_model;
import com.edu_touch.edu_hunt.Model.subject_model;
import com.edu_touch.edu_hunt.Payment;
import com.edu_touch.edu_hunt.R;
import com.edu_touch.edu_hunt.Show_Teachers;
import com.edu_touch.edu_hunt.clicky;
import com.edu_touch.edu_hunt.volley.CustomRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Book_Now_Adapter extends RecyclerView.Adapter<Book_Now_Adapter.GithubViewHolder> {
    SharedPreferences sharedPreferences;

     Context context;
     ArrayList<select_model> data;
     public static clicky clicky;
    public Book_Now_Adapter(Context context, ArrayList<select_model> data,clicky clicky){
        this.context = context;
        Book_Now_Adapter.clicky =clicky;
        this.data= data;
    }

    @NonNull
    @Override
    public Book_Now_Adapter.GithubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.select_now_cardview,viewGroup,false);
        return new GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubViewHolder holder, int position) {

        holder.subject.setText("Subject : "+data.get(position).getSubjects());

        holder.fee.setText("Fees : "+data.get(position).getFees());
        holder.classes.setText("Class : "+data.get(position).getClass_name());
        holder.board.setText("Board : "+data.get(position).getBoards());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                if (data.get(position).getFees().equals("-")) {
                    Toast.makeText(context, "You cannot Apply for this Subject", Toast.LENGTH_SHORT).show();
                } else {
                    if (sharedPreferences.getString("status", "null").equals("0")) {
                        getingfee(data.get(position).getSubjects_id(), data.get(position).getClass_id()
                                , data.get(position).getBoards_id(), data.get(position).getFees());
                    } else {
                        Book_Now_Adapter.clicky.onclicky(data.get(position).getSubjects_id(), data.get(position).getClass_id()
                                , data.get(position).getBoards_id(), data.get(position).getFees());
                    }
                }
            }
        });
    }


    private void getingfee(String subjects_id, String class_id, String boards_id, String fees) {
        final AlertDialog loading = new ProgressDialog(context);
        loading.setMessage("Getting Info....");
        loading.setCancelable(false);
        loading.show();

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url_checkingfee, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String code = response.getString("error code");
                    if (code.equals("200")){

                        JSONArray jsonArray = response.getJSONArray("teachers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);

                            Intent intent = new Intent(context, Payment.class);
                            intent.putExtra("fee",object.getString("registration_fees"));

                            intent.putExtra("subjects_id",subjects_id);
                            intent.putExtra("class_id",class_id);
                            intent.putExtra("boards_id",boards_id);
                            intent.putExtra("feess",fees);
                            context.startActivity(intent);
                            loading.dismiss();

                        }

                    }
                    else {
                        loading.dismiss();
                        //Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                      loading.dismiss();
                      e.printStackTrace();
                    //Toasty.error(context, "Error", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toasty.error(context, "Connection Timed Out", Toast.LENGTH_SHORT, true).show();
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
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonRequest);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class GithubViewHolder extends RecyclerView.ViewHolder{

        TextView board,classes,fee,subject;
        ElasticButton button;
        public GithubViewHolder(@NonNull View itemView) {
            super(itemView);

            board = itemView.findViewById(R.id.board);
            classes = itemView.findViewById(R.id.classi);
            fee = itemView.findViewById(R.id.fees);
            subject = itemView.findViewById(R.id.subjecti);
            button = itemView.findViewById(R.id.button);
        }
    }
}

