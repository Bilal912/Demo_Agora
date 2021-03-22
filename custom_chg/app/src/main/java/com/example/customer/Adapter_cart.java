package com.example.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_cart extends RecyclerView.Adapter<Adapter_cart.ViewHolder>{
    Context context;
    ArrayList<Cart_model> ar;
    ArrayList<String> array;
    public Adapter_cart(Context context, ArrayList<Cart_model> ar, ArrayList<String> array) {
        this.context=context;
        this.ar=ar;
        this.array=array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.b_row_carty,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(ar.get(position).getPname());
        holder.price.setText("Price: "+ar.get(position).getPprice());
        holder.category.setText(ar.get(position).getCategory());
        Picasso.get().load(ar.get(position).getImg()).into(holder.image);
        holder.quantity.setText("Quantity : "+ar.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return ar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name,price,category,quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.tv_subcat_title);
            price=itemView.findViewById(R.id.tv_subcat_price);
            category=itemView.findViewById(R.id.category);

            image=itemView.findViewById(R.id.iv_subcat_img);

            quantity = itemView.findViewById(R.id.iv_subcat_plus);

        }
    }
}
