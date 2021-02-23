package com.edu_touch.edu_hunt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu_touch.edu_hunt.Model.teacher_model;
import com.edu_touch.edu_hunt.R;
import com.edu_touch.edu_hunt.Teacher_detail;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Small_top_Teacher_Adapter extends RecyclerView.Adapter<Small_top_Teacher_Adapter.GithubViewHolder> {
     Context context;
     ArrayList<teacher_model> data;
    public Small_top_Teacher_Adapter(Context context, ArrayList<teacher_model> data){
        this.context = context;
        this.data= data;
    }

    @NonNull
    @Override
    public Small_top_Teacher_Adapter.GithubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.book_two_cardview,viewGroup,false);
        return new GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubViewHolder holder, int position) {

        Random r = new Random();
        position = r.nextInt(data.size());
        Glide.with(context)
                .load(data.get(position).getT_image())
                .centerCrop()
                .placeholder(R.drawable.user2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.imageView);

        holder.name.setText(data.get(position).getTeacher_name());
        holder.expericne.setText(data.get(position).getExperience());
        holder.quali.setText(data.get(position).getQualification());
        if (data.get(position).getSubjects().contains("|")){
            String currentString = data.get(position).getSubjects();
            String[] separated = currentString.split("\\|");
            holder.subject.setText(separated[0]);
        }
        else {
            holder.subject.setText(data.get(position).getSubjects());
        }
        int finalPosition1 = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Teacher_detail.class);
                i.putExtra("id",data.get(finalPosition1).getId());
                i.putExtra("name",data.get(finalPosition1).getTeacher_name());
                i.putExtra("city",data.get(finalPosition1).getCity());
                i.putExtra("address",data.get(finalPosition1).getAddress());
                i.putExtra("qualification",data.get(finalPosition1).getQualification());
                i.putExtra("experience",data.get(finalPosition1).getExperience());
                i.putExtra("image",data.get(finalPosition1).getT_image());
                i.putExtra("code",data.get(finalPosition1).getTeacher_code());
                i.putExtra("subject_id", data.get(finalPosition1).getSubjects_id());
                i.putExtra("board_id", data.get(finalPosition1).getBoards_id());
                i.putExtra("class_id", data.get(finalPosition1).getClass_id());

                i.putExtra("subject",data.get(finalPosition1).getSubjects());
                i.putExtra("board",data.get(finalPosition1).getBoards());
                i.putExtra("fee",data.get(finalPosition1).getFees());
                i.putExtra("class",data.get(finalPosition1).getClass_name());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Math.min(data.size(), 5);
    }

    public static class GithubViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView name,quali,expericne,subject;
        public GithubViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            quali = itemView.findViewById(R.id.text);
            expericne = itemView.findViewById(R.id.exper);
            subject = itemView.findViewById(R.id.subject);
            imageView = itemView.findViewById(R.id.icon);


        }
    }
}

