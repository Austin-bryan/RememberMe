package com.example.rememberme;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimedEventView extends LinearLayout {
    private TextView timeTextView;
    private EventChipView eventChipView;

    private String timeText = "9 AM";
    private String eventText = "Event";
    private float eventTextSizePx;
    private int eventColor = Color.parseColor("#4D8DFF");
    private boolean eventConfirmed = true;
    private boolean eventDarkText = false;
    private int eventPaddingHorizontalPx;
    private int eventPaddingVerticalPx;

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

        eventTextSizePx = spToPx(12f);
        eventPaddingHorizontalPx = dpToPx(8);
        eventPaddingVerticalPx = dpToPx(4);

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

            eventTextSizePx = typedArray.getDimension(
                    R.styleable.TimedEventView_eventTextSize,
                    spToPx(12f)
            );

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

            eventPaddingHorizontalPx = typedArray.getDimensionPixelSize(
                    R.styleable.TimedEventView_eventPaddingHorizontal,
                    dpToPx(8)
            );

            eventPaddingVerticalPx = typedArray.getDimensionPixelSize(
                    R.styleable.TimedEventView_eventPaddingVertical,
                    dpToPx(4)
            );

            typedArray.recycle();
        }

        applyState();
    }

    private void applyState() {
        timeTextView.setText(timeText);
        eventChipView.setEventText(eventText);
        eventChipView.setEventTextSizePx(eventTextSizePx);
        eventChipView.setEventColor(eventColor);
        eventChipView.setConfirmed(eventConfirmed);
        eventChipView.setDarkText(eventDarkText);
        eventChipView.setEventPaddingPx(eventPaddingHorizontalPx, eventPaddingVerticalPx);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private float spToPx(float sp) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
        applyState();
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
        applyState();
    }

    public void setEventTextSizeSp(float eventTextSizeSp) {
        this.eventTextSizePx = spToPx(eventTextSizeSp);
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

    public void setEventPaddingPx(int horizontalPx, int verticalPx) {
        this.eventPaddingHorizontalPx = horizontalPx;
        this.eventPaddingVerticalPx = verticalPx;
        applyState();
    }
}