package com.example.rememberme;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewEventActivity extends AppCompatActivity {
    private String titleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleStr = getIntent().getStringExtra("title");

        setContentView(R.layout.activity_new_event);
        TextView title = findViewById(R.id.newEventTitle);

        if (titleStr != null && titleStr != "") {
            title.setText(titleStr);
        }
    }
}