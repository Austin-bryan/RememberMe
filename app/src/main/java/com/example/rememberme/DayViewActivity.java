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
    private android.widget.LinearLayout dayEventsContainer;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        selectedDate = getIntent().getStringExtra("selectedDate");
        currentUsername = getIntent().getStringExtra("currentUsername");
        databaseHelper = new DatabaseHelper(this);
        dayEventsContainer = findViewById(R.id.dayEventsContainer);

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
            intent.putExtra("selectedDate", selectedDate);

            startActivity(intent);
        });

        if (selectedDate != null) {
            dayHeaderText.setText(formatDateForHeader(selectedDate));
        }
    }

    // auto update even when returning
    @Override
    protected void onResume() {
        super.onResume();
        reloadEventsForSelectedDay();
    }

    private void reloadEventsForSelectedDay() {
        if (dayEventsContainer == null) {
            return;
        }

        dayEventsContainer.removeAllViews();

        if (currentUsername == null
            || currentUsername.isEmpty()
            || selectedDate == null
            || selectedDate.isEmpty()
        ) {
            return;
        }

        var events = databaseHelper.getEventsForUserAndDate(
            currentUsername,
            selectedDate
        );

        for (DatabaseHelper.EventRecord eventRecord : events) {
            TimedEventView timedEventView = new TimedEventView(this);

            timedEventView.setTimeText(eventRecord.time);
            timedEventView.setEventText(eventRecord.name);
            timedEventView.setEventTextSizeSp(12f);
            timedEventView.setEventPaddingPx(dpToPx(8), dpToPx(4));
            timedEventView.setEventColor(getColor(R.color.event_default));
            timedEventView.setEventConfirmed(true);
            timedEventView.setEventDarkText(false);

            var layoutParams =
                new android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                );

            layoutParams.topMargin = dpToPx(8);
            timedEventView.setLayoutParams(layoutParams);

            timedEventView.setOnClickListener(view -> {
                Intent intent = new android.content.Intent(DayViewActivity.this, EventActivity.class);

                intent.putExtra("username", currentUsername);
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("eventId", eventRecord.id);
                intent.putExtra("title", eventRecord.name);
                intent.putExtra("eventTime", eventRecord.time);
                intent.putExtra("eventDescription", eventRecord.description);

                startActivity(intent);
            });

            dayEventsContainer.addView(timedEventView);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
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