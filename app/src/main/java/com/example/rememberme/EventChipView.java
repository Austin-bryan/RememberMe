package com.example.rememberme;

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
    private int eventColor = Color.parseColor("#4D8DFF");
    private boolean eventConfirmed = true;
    private boolean eventDarkText = false;

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

            typedArray.recycle();
        }

        applyState();
    }

    private void applyState() {
        eventChipText.setText(eventText);
        eventChipText.setTextColor(eventDarkText ? Color.BLACK : Color.WHITE);

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

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
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