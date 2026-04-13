package com.example.rememberme;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconTextView extends LinearLayout {
    private ImageView iconView;
    private TextView textView;

    public IconTextView(Context context) {
        super(context);
        init(context, null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.view_icon_text, this, true);

        iconView = findViewById(R.id.iconView);
        textView = findViewById(R.id.textView);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconTextView);

            int iconRes = typedArray.getResourceId(R.styleable.IconTextView_rowIcon, 0);
            if (iconRes != 0) {
                iconView.setImageResource(iconRes);
            }

            String rowText = typedArray.getString(R.styleable.IconTextView_rowText);
            if (rowText != null) {
                textView.setText(rowText);
            }

            if (typedArray.hasValue(R.styleable.IconTextView_rowTextColor)) {
                int textColor = typedArray.getColor(
                    R.styleable.IconTextView_rowTextColor,
                    textView.getCurrentTextColor()
                );
                textView.setTextColor(textColor);
            }

            if (typedArray.hasValue(R.styleable.IconTextView_rowIconTint)) {
                int iconTint = typedArray.getColor(R.styleable.IconTextView_rowIconTint, 0);
                iconView.setColorFilter(iconTint);
            }

            typedArray.recycle();
        }
    }

    public void setRowIcon(int iconRes) {
        iconView.setImageResource(iconRes);
    }

    public void setRowText(String text) {
        textView.setText(text);
    }

    public void setRowTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setRowIconTint(int color) {
        iconView.setColorFilter(color);
    }
}