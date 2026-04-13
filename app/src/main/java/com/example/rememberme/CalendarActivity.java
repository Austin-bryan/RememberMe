package com.example.rememberme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private String currentUsername;
    private final List<String> days = List.of(
            "Mar 1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12", "13", "14",
            "15", "16", "17", "18", "19", "20", "21",
            "22", "23", "24", "25", "26", "27", "28",
            "29", "30", "31", "Apr 1", "2", "3", "4"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUsername = getIntent().getStringExtra("currentUsername");
        setContentView(R.layout.activity_calendar);
        populateCalendarDays();
    }

    private void populateCalendarDays() {
        LinearLayout calendarGrid = findViewById(R.id.calendarGrid);
        int dayIndex = 0;

        for (int rowIndex = 0; rowIndex < calendarGrid.getChildCount(); rowIndex++) {
            View rowView = calendarGrid.getChildAt(rowIndex);

            if (!(rowView instanceof LinearLayout)) {
                continue;
            }

            LinearLayout weekRow = (LinearLayout) rowView;

            for (int columnIndex = 0; columnIndex < weekRow.getChildCount(); columnIndex++) {
                View dayCellView = weekRow.getChildAt(columnIndex);
                TextView dayNumberText = dayCellView.findViewById(R.id.dayNumberText);

                if (dayNumberText == null) {
                    continue;
                }

                dayNumberText.setText(
                    dayIndex < days.size() ? days.get(dayIndex) : ""
                );

                String dayStr = String.valueOf((dayIndex % 31) + 1);
                if (dayStr.length() < 10) {
                    dayStr = "0" + dayStr;
                }

                // if is april
                if (dayIndex >= 31) {
                    dayCellView.setTag("2026-04-" + dayStr);
                    dayCellView.setBackgroundResource(R.drawable.bg_calendar_day_next);
                // else is march
                } else {
                    dayCellView.setTag("2026-03-" + dayStr);
                }

                dayCellView.setOnClickListener(view -> {
                    String selectedDate = (String) view.getTag();

                    Intent intent = new Intent(CalendarActivity.this, DayViewActivity.class);
                    intent.putExtra("selectedDate", selectedDate);
                    intent.putExtra("currentUsername", currentUsername);
                    startActivity(intent);
                });

                dayIndex++;
            }
        }
    }
}