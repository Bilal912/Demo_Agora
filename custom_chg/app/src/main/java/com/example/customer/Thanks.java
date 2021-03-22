package com.example.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trenzlr.firebasenotificationhelper.FirebaseNotiCallBack;
import com.trenzlr.firebasenotificationhelper.FirebaseNotificationHelper;

import java.util.Map;

public class Thanks extends AppCompatActivity implements FirebaseNotiCallBack {

    String ven;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

        Toast.makeText(this, "Order Successfull", Toast.LENGTH_SHORT).show();

        db = FirebaseDatabase.getInstance().getReference();
        ven = getIntent().getStringExtra("ven");

        db.child("Token").child(ven).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                        Map<String, String> map = (Map<String, String>) snapshot.getValue();
                        sendNotify(map.get("device"));
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendNotify(String device) {
        try {
            FirebaseNotificationHelper.initialize(getResources().getString(R.string.server_key))
                    .defaultJson(true, null)
                    .title("New Order")
                    .message("Check Your Orders")
                    .setCallBack(Thanks.this)
                    .receiverFirebaseToken(device)
                    .send();

        } catch (Exception ignored){
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Thanks.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

    //Result of Sending Notification
    @Override
    public void success(String s) {
    }

    @Override
    public void fail(String s) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}