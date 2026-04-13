package com.example.rememberme;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class EventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        AppCompatImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());

    }
}