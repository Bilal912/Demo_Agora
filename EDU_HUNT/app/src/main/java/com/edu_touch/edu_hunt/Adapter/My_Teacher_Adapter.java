package com.edu_touch.edu_hunt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu_touch.edu_hunt.Model.payment_history_model;
import com.edu_touch.edu_hunt.R;
import com.edu_touch.edu_hunt.Teacher_detail;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_Teacher_Adapter extends RecyclerView.Adapter<My_Teacher_Adapter.GithubViewHolder> {
     Context context;
     ArrayList<payment_history_model> data;
     String imagelink;
    public My_Teacher_Adapter(Context context, ArrayList<payment_history_model> data, String imagelink){
        this.context = context;
        this.data= data;
        this.imagelink=imagelink;
    }

    @NonNull
    @Override
    public My_Teacher_Adapter.GithubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cadview_myteacher,viewGroup,false);
        return new GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubViewHolder holder, int position) {

        Glide.with(context)
                .load(imagelink+data.get(position).getT_image())
                .centerCrop()
                .placeholder(R.drawable.user2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.imageView);

        holder.name.setText(data.get(position).getTeacher_Name());

        holder.expericne.setText("Booking Date : "+data.get(position).getBooking_date());
        holder.quali.setText(data.get(position).getClass_Groups());
        if (data.get(position).getStudent_Name().contains("|")){
            String currentString = data.get(position).getStudent_Name();
            String[] separated = currentString.split("\\|");
            holder.subject.setText(separated[0]);
        }
        else {
            holder.subject.setText(data.get(position).getStudent_Name());
        }
        holder.amount.setText(context.getResources().getString(R.string.currency)+" "+data.get(position).getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


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

        CircleImageView imageView;
        TextView name,quali,expericne,subject,amount;
        public GithubViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            name = itemView.findViewById(R.id.name);
            quali = itemView.findViewById(R.id.text);
            expericne = itemView.findViewById(R.id.exper);
            subject = itemView.findViewById(R.id.subject);
            imageView = itemView.findViewById(R.id.icon);

        }
    }
}

