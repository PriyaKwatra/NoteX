package com.example.acer.notex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificatio);
        Intent i=getIntent();
        i.getStringExtra("PENDING");
        TextView tView=(TextView)findViewById(R.id.noten);
        tView.setText(i.getStringExtra("PENDING"));
    }
}
