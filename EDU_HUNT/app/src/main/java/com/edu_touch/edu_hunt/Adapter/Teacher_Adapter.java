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
import com.edu_touch.edu_hunt.Model.subject_model;
import com.edu_touch.edu_hunt.Model.teacher_model;
import com.edu_touch.edu_hunt.R;
import com.edu_touch.edu_hunt.Show_Teachers;
import com.edu_touch.edu_hunt.Teacher_detail;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Teacher_Adapter extends RecyclerView.Adapter<Teacher_Adapter.GithubViewHolder> {
     Context context;
     ArrayList<teacher_model> data;
    public Teacher_Adapter(Context context, ArrayList<teacher_model> data){
        this.context = context;
        this.data= data;
    }

    @NonNull
    @Override
    public Teacher_Adapter.GithubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.book_now_cardview,viewGroup,false);
        return new GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubViewHolder holder, int position) {

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Teacher_detail.class);
                i.putExtra("id", data.get(position).getId());
                i.putExtra("name", data.get(position).getTeacher_name());
                i.putExtra("city", data.get(position).getCity());
                i.putExtra("address", data.get(position).getAddress());
                i.putExtra("qualification", data.get(position).getQualification());
                i.putExtra("experience", data.get(position).getExperience());
                i.putExtra("image", data.get(position).getT_image());
                i.putExtra("code", data.get(position).getTeacher_code());

                i.putExtra("subject", data.get(position).getSubjects());
                i.putExtra("board", data.get(position).getBoards());
                i.putExtra("fee", data.get(position).getFees());
                i.putExtra("class", data.get(position).getClass_name());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
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

