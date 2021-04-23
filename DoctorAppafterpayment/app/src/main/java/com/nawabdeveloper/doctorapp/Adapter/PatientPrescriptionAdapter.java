package com.nawabdeveloper.doctorapp.Adapter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.nawabdeveloper.doctorapp.Constant;
import com.nawabdeveloper.doctorapp.Model.Doctor_Model;
import com.nawabdeveloper.doctorapp.Model.Prescription_model;
import com.nawabdeveloper.doctorapp.R;
import com.nawabdeveloper.doctorapp.ShowImage;
import com.nawabdeveloper.doctorapp.ShowText;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PatientPrescriptionAdapter extends RecyclerView.Adapter<PatientPrescriptionAdapter.MyHolder> {
    private final Context context;

    TextView no_data;
    ProgressDialog pg;
    ArrayList<Prescription_model> ar;
    ArrayList<Prescription_model> point;
    public PatientPrescriptionAdapter(ArrayList<Prescription_model> ar, Context context, ProgressDialog pg, TextView no_data) {
        this.ar=ar;
        this.context=context;
        this.pg=pg;
        this.no_data=no_data;
        this.point = new ArrayList<>(ar);
    }

    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_perscritions, parent, false);
        return new MyHolder(v);
    }

    @Override
    public int getItemCount() {
        return ar.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Prescription_model model = ar.get(position);

        if (model.getType().equals("text")){
            holder.file.setBackground(context.getResources().getDrawable(R.drawable.iconfile));
            holder.name.setText(model.getDoc_name());
            holder.Date.setText(model.getDate()+" , "+model.getTime());
            String s = model.getText();
            String upToNCharacters = s.substring(0, Math.min(s.length(), 10));
            holder.pres.setText(upToNCharacters+".... Read more");

        } else if (model.getType().equals("image")){
            holder.file.setBackground(context.getResources().getDrawable(R.drawable.iconimage));
            holder.name.setText(model.getDoc_name());
            holder.Date.setText(model.getDate()+" , "+model.getTime());
            holder.pres.setText("Click to Open Prescription Image");
        }
        else if (model.getType().equals("pdf")){
            holder.file.setBackground(context.getResources().getDrawable(R.drawable.iconpdf));
            holder.name.setText(model.getDoc_name());
            holder.Date.setText(model.getDate()+" , "+model.getTime());
            holder.pres.setText("Click to Downlaod Prescription Document");
        }
        else {
            holder.cardview.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getType().equals("text")){
                    Intent i = new Intent(context, ShowText.class);
                    i.putExtra(Constant.image,model.getText());
                    context.startActivity(i);
                } else if (model.getType().equals("image")){
                    Intent i = new Intent(context, ShowImage.class);
                    i.putExtra(Constant.image,model.getImage());
                    context.startActivity(i);
                }
                else if (model.getType().equals("pdf")){
                    File storageDir = null;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        storageDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));
                    }
                    final File finalStorageDir = storageDir;

                    downloadFile(context,model.getDoc_name()+"Result",".pdf",
                            finalStorageDir.getAbsolutePath(),model.getImage());

                }
            }
        });

    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView name,pres,Date;
        CardView cardview;
        LinearLayout file;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            file = itemView.findViewById(R.id.file);
            cardview= itemView.findViewById(R.id.cardview);
            name = itemView.findViewById(R.id.name);
            Date = itemView.findViewById(R.id.order_date);
            pres = itemView.findViewById(R.id.pres);


        }
    }

    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Prescription_model> arrayList = new ArrayList<>();
            if(charSequence.toString().isEmpty()) {
                arrayList.addAll(point);
            }else
            {
                for(Prescription_model point: point){
                    if(point.getDoc_name().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        arrayList.add(point);
                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ar.clear();
            ar.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }

}
