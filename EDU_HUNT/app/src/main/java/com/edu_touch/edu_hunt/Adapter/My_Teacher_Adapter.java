package com.edu_touch.edu_hunt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu_touch.edu_hunt.Confirmation;
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
        holder.quali.setText("Class : "+data.get(position).getClass_Groups());
        if (data.get(position).getSubject_Name().contains("|")){
            String currentString = data.get(position).getStudent_Name();
            String[] separated = currentString.split("\\|");
            holder.subject.setText("Subject : "+separated[0]);
        }
        else {
            holder.subject.setText("Subject : "+data.get(position).getSubject_Name());
        }
        holder.amount.setText(context.getResources().getString(R.string.currency)+" "+data.get(position).getAmount());

        if (data.get(position).getStarting_status().equals("0")){
            holder.flag.setImageResource(R.drawable.red_flag);
            holder.start_date.setVisibility(View.GONE);
        }
        else {
            holder.start_date.setVisibility(View.VISIBLE);
            holder.start_date.setText("Starting Date : "+data.get(position).getStarting_date());
            holder.flag.setImageResource(R.drawable.green_flag);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getStarting_status().equals("0")){
                    Intent i = new Intent(context, Confirmation.class);
                    i.putExtra("id",data.get(position).getId());
                    context.startActivity(i);}
                else {
                    Toast.makeText(context, "Already Started", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class GithubViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        ImageView flag;
        TextView name,quali,expericne,subject,amount,start_date;
        public GithubViewHolder(@NonNull View itemView) {
            super(itemView);

            start_date = itemView.findViewById(R.id.startdate);
            flag = itemView.findViewById(R.id.flag_id);
            amount = itemView.findViewById(R.id.amount);
            name = itemView.findViewById(R.id.name);
            quali = itemView.findViewById(R.id.text);
            expericne = itemView.findViewById(R.id.exper);
            subject = itemView.findViewById(R.id.subject);
            imageView = itemView.findViewById(R.id.icon);

        }
    }
}

