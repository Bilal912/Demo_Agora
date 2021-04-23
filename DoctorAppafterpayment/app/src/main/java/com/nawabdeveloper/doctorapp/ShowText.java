package com.nawabdeveloper.doctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowText extends AppCompatActivity {
TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        getSupportActionBar().hide();

        text = findViewById(R.id.text);

        text.setText(getIntent().getStringExtra(Constant.image));

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Prescription", text.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ShowText.this, "Copy into your Clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}