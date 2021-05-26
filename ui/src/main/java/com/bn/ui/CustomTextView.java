package com.bn.ui;

import android.content.Context;
import android.util.AttributeSet;

class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {


    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setTextSize(int unit, float size) {
        float newSize = size * 3f;
        super.setTextSize(unit, newSize);
    }
}
