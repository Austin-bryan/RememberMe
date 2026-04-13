package com.example.rememberme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.button.MaterialButton;

public class EventActivity extends AppCompatActivity {
    private String currentUsername;
    private String selectedDate;
    private int eventId = -1;
    private String eventTitle;
    private String eventTime;
    private String eventDescription;

    private DatabaseHelper databaseHelper;

    private TextView eventTitleText;
    private IconTextView eventDateTimeRow;
    private IconTextView eventDescriptionRow;

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

        databaseHelper = new DatabaseHelper(this);

        eventTitleText = findViewById(R.id.eventTitleText);
        eventDateTimeRow = findViewById(R.id.eventDateTimeRow);
        eventDescriptionRow = findViewById(R.id.eventDescriptionRow);

        AppCompatImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());

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

        MaterialButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(view -> deleteCurrentEvent());

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

    @Override
    protected void onResume() {
        super.onResume();
        reloadEvent();
    }

    // Reload when event is edited and needs a refresh
    private void reloadEvent() {
        if (eventId == -1) {
            return;
        }

        DatabaseHelper.EventRecord eventRecord = databaseHelper.getEventById(eventId);
        if (eventRecord == null) {
            return;
        }

        eventTitle = eventRecord.name;
        selectedDate = eventRecord.date;
        eventTime = eventRecord.time;
        eventDescription = eventRecord.description;

        if (eventTitle != null && !eventTitle.isEmpty()) {
            eventTitleText.setText(eventTitle);
        }

        String formattedDateTime = formatDateForEventRow(selectedDate, eventTime);
        eventDateTimeRow.setRowText(formattedDateTime);

        if (eventDescription != null && !eventDescription.isEmpty()) {
            eventDescriptionRow.setRowText(eventDescription);
        } else {
            eventDescriptionRow.setRowText("No description");
        }
    }

    private void deleteCurrentEvent() {
        if (eventId == -1) {
            Toast.makeText(
                this,
                "No event to delete.",
                Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean deleted = databaseHelper.deleteEvent(eventId);

        if (!deleted) {
            Toast.makeText(
                this,
                "Unable to delete event.",
                Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Toast.makeText(
            this,
            "Event deleted.",
            Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}