package com.example.rememberme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class EventActivity extends AppCompatActivity {
    private String currentUsername;
    private String selectedDate;
    private int eventId = -1;
    private String eventTitle;
    private String eventTime;
    private String eventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        currentUsername = getIntent().getStringExtra("username");
        selectedDate = getIntent().getStringExtra("selectedDate");
        eventId = getIntent().getIntExtra("eventId", -1);
        eventTitle = getIntent().getStringExtra("title");
        eventTime = getIntent().getStringExtra("eventTime");
        eventDescription = getIntent().getStringExtra("eventDescription");

        AppCompatImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());

        TextView eventTitleText = findViewById(R.id.eventTitleText);
        IconTextView eventDateTimeRow = findViewById(R.id.eventDateTimeRow);
        IconTextView eventDescriptionRow = findViewById(R.id.eventDescriptionRow);

        if (eventTitle != null && !eventTitle.isEmpty()) {
            eventTitleText.setText(eventTitle);
        }

        AppCompatImageButton editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(view -> {
            Intent intent = new Intent(
                EventActivity.this,
                NewEventActivity.class
            );

            intent.putExtra("username", currentUsername);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("eventId", eventId);
            intent.putExtra("title", "Update Event");

            intent.putExtra("eventName", eventTitle);
            intent.putExtra("eventTime", eventTime);
            intent.putExtra("eventDescription", eventDescription);


            startActivity(intent);
        });

        String formattedDateTime = formatDateForEventRow(selectedDate, eventTime);
        eventDateTimeRow.setRowText(formattedDateTime);

        if (eventDescription != null && !eventDescription.isEmpty()) {
            eventDescriptionRow.setRowText(eventDescription);
        } else {
            eventDescriptionRow.setRowText("No description");
        }
    }

    private String formatDateForEventRow(String selectedDate, String eventTime) {
        if (selectedDate == null || selectedDate.isEmpty()) {
            return eventTime != null ? eventTime : "";
        }

        String[] parts = selectedDate.split("-");
        if (parts.length != 3) {
            return eventTime != null && !eventTime.isEmpty()
                ? selectedDate + " • " + eventTime
                : selectedDate;
        }

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

        String base = monthName + " " + Integer.parseInt(day);

        if (eventTime != null && !eventTime.isEmpty()) {
            return base + " • " + eventTime;
        }

        return base;
    }
}