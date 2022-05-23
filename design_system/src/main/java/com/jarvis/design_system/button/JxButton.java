package com.jarvis.design_system.button;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.jarvis.design_system.textview.CustomTextView;

public class JxButton extends CustomTextView {
    public JxButton(Context context) {
        super(context);
    }

    public JxButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JxButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void enable() {
        this.setEnabled(true);
    }

    public void disable() {
        this.setEnabled(false);
    }

    public void setTextColorButton(int colorId) {
        this.setTextColor(ContextCompat.getColor(getContext(), colorId));
    }

    public void setStartIcon(int resource) {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), resource), null, null, null);
    }
}
