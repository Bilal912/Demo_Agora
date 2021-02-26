package com.edu_touch.edu_hunt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu_touch.edu_hunt.Confirmation;
import com.edu_touch.edu_hunt.Model.fee_model;
import com.edu_touch.edu_hunt.Model.payment_history_model;
import com.edu_touch.edu_hunt.Payment;
import com.edu_touch.edu_hunt.R;
import com.skydoves.elasticviews.ElasticButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fee_Adapter extends RecyclerView.Adapter<Fee_Adapter.GithubViewHolder> {
     Context context;
     ArrayList<fee_model> data;
     String imagelink;
    public Fee_Adapter(Context context, ArrayList<fee_model> data){
        this.context = context;
        this.data= data;
        this.imagelink=imagelink;
    }

    @NonNull
    @Override
    public Fee_Adapter.GithubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cardview_fees,viewGroup,false);
        return new GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubViewHolder holder, int position) {


        holder.name.setText(data.get(position).getTeacher_Name()+"\n"+
                "Subject : "+data.get(position).getSubject_Name());

        holder.amount.setText(context.getResources().getString(R.string.currency)+" "+data.get(position).getAmount()+" /-");

        if (data.get(position).getStatus().equals("0")){
            holder.paid.setVisibility(View.GONE);
            holder.button.setVisibility(View.VISIBLE);

        }
        else {
            holder.paid.setVisibility(View.VISIBLE);
            holder.button.setVisibility(View.GONE);

        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Payment.class);
                intent.putExtra("fee","Fee Deposit");
                intent.putExtra("amount",data.get(position).getAmount());
                intent.putExtra("fee_id",data.get(position).getId());
                intent.putExtra("booking_id",data.get(position).getBooking_id());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class GithubViewHolder extends RecyclerView.ViewHolder{

        ElasticButton button;
        TextView name,amount,paid;
        public GithubViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.price_id);
            name = itemView.findViewById(R.id.namey);
            paid = itemView.findViewById(R.id.paid);
            button = itemView.findViewById(R.id.paynow);

        }
    }
}

