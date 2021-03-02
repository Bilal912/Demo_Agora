package com.edu_touch.edu_hunt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edu_touch.edu_hunt.Model.Notification_model;
import com.edu_touch.edu_hunt.Model.subject_model;
import com.edu_touch.edu_hunt.R;
import com.edu_touch.edu_hunt.Show_Teachers;

import java.util.ArrayList;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.GithubViewHolder> {
     Context context;
     ArrayList<Notification_model> data;
    public Notification_Adapter(Context context, ArrayList<Notification_model> data){
        this.context = context;
        this.data= data;
    }

    @NonNull
    @Override
    public Notification_Adapter.GithubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cardview_notify,viewGroup,false);
        return new GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubViewHolder holder, int position) {

        holder.name.setText(data.get(position).getMessage()
                +"\nBooking Date : "+ data.get(position).getDate()
        );

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class GithubViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        public GithubViewHolder(@NonNull View itemView) {
            super(itemView);
        name = itemView.findViewById(R.id.text);

        }
    }
}

