package com.edu_touch.edu_hunt.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edu_touch.edu_hunt.Model.payment_history_model;
import com.edu_touch.edu_hunt.Model.subscription_model;
import com.edu_touch.edu_hunt.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Subscription_Payment_History_Adapter extends RecyclerView.Adapter<Subscription_Payment_History_Adapter.GithubViewHolder> {
     Context context;
     ArrayList<subscription_model> data;
     SharedPreferences sharedPreferences;
    public Subscription_Payment_History_Adapter(Context context, ArrayList<subscription_model> data){
        this.context = context;
        this.data= data;
        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Subscription_Payment_History_Adapter.GithubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cadview_paymenthistory,viewGroup,false);
        return new GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubViewHolder holder, int position) {

        holder.name.setText(sharedPreferences.getString("name","null"));
        holder.fee.setText(context.getResources().getString(R.string.currency)+" "+data.get(position).getAmount()+" ");

        String temp = data.get(position).getDate();
        String[] avy = temp.split("-");
        holder.date.setText(avy[2]);

        holder.tempy.setText("Subscription Payment");
        holder.month.setText(checkmonth(avy[1]));
    }

    public String checkmonth(String s) {
        String monthy = null;
        if (s.equals("01") || s.equals("1")){
            monthy = "Jan";
        }
        else if (s.equals("02") || s.equals("2")){
            monthy = "Feb";
        }
        else if (s.equals("03") || s.equals("3")){
            monthy = "Mar";
        }else if (s.equals("04") || s.equals("4")){
            monthy = "Apr";
        }else if (s.equals("05") || s.equals("5")){
            monthy = "May";
        }else if (s.equals("06") || s.equals("6")){
            monthy = "Jun";
        }else if (s.equals("07") || s.equals("7")){
            monthy = "Jul";
        }else if (s.equals("08") || s.equals("8")){
            monthy = "Aug";
        }else if (s.equals("09") || s.equals("9")){
            monthy = "Sep";
        }else if (s.equals("010") || s.equals("10")){
            monthy = "Oct";
        }else if (s.equals("011") || s.equals("11")){
            monthy = "Nov";
        }else if (s.equals("012") || s.equals("12")){
            monthy = "Dec";
        }
        return monthy;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class GithubViewHolder extends RecyclerView.ViewHolder{

        TextView name,date,fee,month,tempy;
        public GithubViewHolder(@NonNull View itemView) {
            super(itemView);

            tempy = itemView.findViewById(R.id.text);
            name = itemView.findViewById(R.id.t_name);
            date = itemView.findViewById(R.id.date);
            fee = itemView.findViewById(R.id.amount);
            month = itemView.findViewById(R.id.month);
        }
    }
}

