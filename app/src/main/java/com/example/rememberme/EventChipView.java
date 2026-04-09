package com.example.rememberme;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class EventChipView extends FrameLayout {
    private TextView eventChipText;

    private String eventText = "Event";
    private float eventTextSize = 8.5f;
    private int eventColor = Color.parseColor("#4D8DFF");
    private boolean eventConfirmed = true;
    private boolean eventDarkText = false;
    private int eventPaddingHorizontal = 4;
    private int eventPaddingVertical = 1;


    public EventChipView(Context context) {
        super(context);
        init(context, null);
    }

    public EventChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EventChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_event_chip, this, true);
        eventChipText = findViewById(R.id.eventChipText);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EventChipView);

            String xmlText = typedArray.getString(R.styleable.EventChipView_eventText);
            if (xmlText != null) {
                eventText = xmlText;
            }

            eventTextSize = typedArray.getDimension(
                    R.styleable.EventChipView_eventTextSize,
                    spToPx(8)
            );

            eventColor = typedArray.getColor(
                    R.styleable.EventChipView_eventColor,
                    Color.parseColor("#4D8DFF")
            );

            eventConfirmed = typedArray.getBoolean(
                    R.styleable.EventChipView_eventConfirmed,
                    true
            );

            eventDarkText = typedArray.getBoolean(
                    R.styleable.EventChipView_eventDarkText,
                    false
            );

            eventPaddingHorizontal = typedArray.getDimensionPixelSize(
                    R.styleable.EventChipView_eventPaddingHorizontal,
                    dpToPx(4)
            );

            eventPaddingVertical = typedArray.getDimensionPixelSize(
                    R.styleable.EventChipView_eventPaddingVertical,
                    dpToPx(1)
            );

            typedArray.recycle();
        }

        applyState();
    }

    private void applyState() {
        eventChipText.setText(eventText);
        eventChipText.setTextColor(eventDarkText ? Color.BLACK : Color.WHITE);
        eventChipText.setTextSize(pxToSp(eventTextSize));
        eventChipText.setPadding(
                eventPaddingHorizontal,
                eventPaddingVertical,
                eventPaddingHorizontal,
                eventPaddingVertical
        );

        GradientDrawable backgroundDrawable = (GradientDrawable) ContextCompat.getDrawable(
                getContext(),
                eventConfirmed ? R.drawable.bg_event_confirmed : R.drawable.bg_event_unconfirmed
        );

        if (backgroundDrawable != null) {
            backgroundDrawable = (GradientDrawable) backgroundDrawable.mutate();

            if (eventConfirmed) {
                backgroundDrawable.setColor(eventColor);
            } else {
                backgroundDrawable.setColor(Color.TRANSPARENT);
                backgroundDrawable.setStroke(dpToPx(2), eventColor);
            }

            eventChipText.setBackground(backgroundDrawable);
        }
    }

    private float pxToSp(float px) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private float spToPx(float sp) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
        applyState();
    }

    public void setEventColor(int eventColor) {
        this.eventColor = eventColor;
        applyState();
    }

    public void setConfirmed(boolean confirmed) {
        this.eventConfirmed = confirmed;
        applyState();
    }

    public void setDarkText(boolean darkText) {
        this.eventDarkText = darkText;
        applyState();
    }
}