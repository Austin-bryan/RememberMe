package com.example.rememberme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.button.MaterialButton;

public class DayViewActivity extends AppCompatActivity {
    private String selectedDate;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        selectedDate = getIntent().getStringExtra("selectedDate");
        currentUsername = getIntent().getStringExtra("currentUsername");

        TextView dayHeaderText = findViewById(R.id.dayHeaderText);

        AppCompatImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());

        MaterialButton eventButton = findViewById(R.id.newEventButton);
        eventButton.setOnClickListener(view -> {
            Intent intent = new Intent(
                DayViewActivity.this,
                NewEventActivity.class
            );

            intent.putExtra("username", currentUsername);
            intent.putExtra("title", "Create New Event");

            startActivity(intent);
        });

        if (selectedDate != null) {
            dayHeaderText.setText(formatDateForHeader(selectedDate));
        }
    }

    private String formatDateForHeader(String selectedDate) {
        if (selectedDate == null) {
            return "Selected Day";
        }

        String[] parts = selectedDate.split("-");
        if (parts.length != 3) {
            return selectedDate;
        }

        String year = parts[0];
        String month = parts[1];
        String day = parts[2];

        String monthName;
        switch (month) {
            case "03":
                monthName = "March";
                break;
            case "04":
                monthName = "April";
                break;
            default:
                monthName = "Unknown";
                break;
        }

        return monthName + " " + Integer.parseInt(day) + ", " + year;
    }
}