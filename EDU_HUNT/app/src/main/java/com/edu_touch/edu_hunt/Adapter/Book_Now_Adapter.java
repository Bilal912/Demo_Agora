package com.edu_touch.edu_hunt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edu_touch.edu_hunt.Model.select_model;
import com.edu_touch.edu_hunt.Model.subject_model;
import com.edu_touch.edu_hunt.Payment;
import com.edu_touch.edu_hunt.R;
import com.edu_touch.edu_hunt.Show_Teachers;
import com.skydoves.elasticviews.ElasticButton;

import java.util.ArrayList;

public class Book_Now_Adapter extends RecyclerView.Adapter<Book_Now_Adapter.GithubViewHolder> {
     Context context;
     ArrayList<select_model> data;
    public Book_Now_Adapter(Context context, ArrayList<select_model> data){
        this.context = context;
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
                Intent i = new Intent(context, Payment.class);
                context.startActivity(i);
            }
        });
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

