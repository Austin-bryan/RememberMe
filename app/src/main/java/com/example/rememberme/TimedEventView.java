package com.example.rememberme;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimedEventView extends LinearLayout {
    private TextView timeTextView;
    private EventChipView eventChipView;

    private String timeText = "9 AM";
    private String eventText = "Event";
    private int eventColor = Color.parseColor("#4D8DFF");
    private boolean eventConfirmed = true;
    private boolean eventDarkText = false;

    public TimedEventView(Context context) {
        super(context);
        init(context, null);
    }

    public TimedEventView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TimedEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.widget_timed_event, this, true);

        timeTextView = findViewById(R.id.timeText);
        eventChipView = findViewById(R.id.eventChip);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimedEventView);

            String xmlTimeText = typedArray.getString(R.styleable.TimedEventView_timeText);
            if (xmlTimeText != null) {
                timeText = xmlTimeText;
            }

            String xmlEventText = typedArray.getString(R.styleable.TimedEventView_eventText);
            if (xmlEventText != null) {
                eventText = xmlEventText;
            }

            eventColor = typedArray.getColor(
                    R.styleable.TimedEventView_eventColor,
                    Color.parseColor("#4D8DFF")
            );

            eventConfirmed = typedArray.getBoolean(
                    R.styleable.TimedEventView_eventConfirmed,
                    true
            );

            eventDarkText = typedArray.getBoolean(
                    R.styleable.TimedEventView_eventDarkText,
                    false
            );

            typedArray.recycle();
        }

        applyState();
    }

    private void applyState() {
        timeTextView.setText(timeText);
        eventChipView.setEventText(eventText);
        eventChipView.setEventColor(eventColor);
        eventChipView.setConfirmed(eventConfirmed);
        eventChipView.setDarkText(eventDarkText);
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
        applyState();
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
        applyState();
    }

    public void setEventColor(int eventColor) {
        this.eventColor = eventColor;
        applyState();
    }

    public void setEventConfirmed(boolean eventConfirmed) {
        this.eventConfirmed = eventConfirmed;
        applyState();
    }

    public void setEventDarkText(boolean eventDarkText) {
        this.eventDarkText = eventDarkText;
        applyState();
    }
}