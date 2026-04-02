package com.example.rememberme;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class IconEditText extends LinearLayout {
    public IconEditText(Context context, AttributeSet attributes) {
        super(context, attributes);

        setOrientation(HORIZONTAL);
        setGravity(android.view.Gravity.CENTER_VERTICAL);

        inflate(context, R.layout.view_icon_edit, this);

        ImageView iconView = findViewById(R.id.inner_icon);
        EditText inputField = findViewById(R.id.widget_input);

        TypedArray typedArray = context.obtainStyledAttributes(attributes, R.styleable.IconEditText);

        int iconRes = typedArray.getResourceId(R.styleable.IconEditText_customIcon, 0);

        if (iconRes != 0) {
            iconView.setImageResource(iconRes);
        }

        String hint = typedArray.getString(R.styleable.IconEditText_customHint);

        if (hint != null) {
            inputField.setHint(hint);
        }

        if (typedArray.getBoolean(R.styleable.IconEditText_isPassword, false)) {
            inputField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        typedArray.recycle();
    }
}