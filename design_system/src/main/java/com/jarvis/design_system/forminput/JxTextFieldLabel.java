package com.jarvis.design_system.forminput;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.jarvis.design_system.R;
import com.jarvis.design_system.textview.CustomTextView;

public class JxTextFieldLabel extends CustomTextView {

    public JxTextFieldLabel(Context context) {
        super(context);
        init();
    }

    public JxTextFieldLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JxTextFieldLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        onNormalState();
    }

    public void onNormalState() {
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.ink_4));
    }

    public void onTypingState() {
//        this.setTextColor(ContextCompat.getColor(getContext(), R.color.brand));
    }

    public void onErrorState() {
//        this.setTextColor(ContextCompat.getColor(getContext(), R.color.bittersweet));
    }

    public void onDisableState() {
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.ink_4));
    }
}
