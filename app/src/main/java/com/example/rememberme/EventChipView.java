package com.example.rememberme;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class EventChipView extends FrameLayout {
    private TextView eventChipText;

    private String eventText = "Event";
    private float eventTextSizePx;
    private int eventColor = Color.parseColor("#4D8DFF");
    private boolean eventConfirmed = true;
    private boolean eventDarkText = false;
    private int eventPaddingHorizontalPx;
    private int eventPaddingVerticalPx;

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

        eventTextSizePx = spToPx(8.5f);
        eventPaddingHorizontalPx = dpToPx(4);
        eventPaddingVerticalPx = dpToPx(1);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EventChipView);

            String xmlText = typedArray.getString(R.styleable.EventChipView_eventText);
            if (xmlText != null) {
                eventText = xmlText;
            }

            eventTextSizePx = typedArray.getDimension(
                    R.styleable.EventChipView_eventTextSize,
                    spToPx(8.5f)
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

            eventPaddingHorizontalPx = typedArray.getDimensionPixelSize(
                    R.styleable.EventChipView_eventPaddingHorizontal,
                    dpToPx(4)
            );

            eventPaddingVerticalPx = typedArray.getDimensionPixelSize(
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
        eventChipText.setTextSize(TypedValue.COMPLEX_UNIT_PX, eventTextSizePx);
        eventChipText.setPadding(
                eventPaddingHorizontalPx,
                eventPaddingVerticalPx,
                eventPaddingHorizontalPx,
                eventPaddingVerticalPx
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

    public void setEventTextSizeSp(float eventTextSizeSp) {
        this.eventTextSizePx = spToPx(eventTextSizeSp);
        applyState();
    }

    public void setEventTextSizePx(float eventTextSizePx) {
        this.eventTextSizePx = eventTextSizePx;
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

    public void setEventPaddingHorizontalPx(int eventPaddingHorizontalPx) {
        this.eventPaddingHorizontalPx = eventPaddingHorizontalPx;
        applyState();
    }

    public void setEventPaddingVerticalPx(int eventPaddingVerticalPx) {
        this.eventPaddingVerticalPx = eventPaddingVerticalPx;
        applyState();
    }

    public void setEventPaddingPx(int horizontalPx, int verticalPx) {
        this.eventPaddingHorizontalPx = horizontalPx;
        this.eventPaddingVerticalPx = verticalPx;
        applyState();
    }
}