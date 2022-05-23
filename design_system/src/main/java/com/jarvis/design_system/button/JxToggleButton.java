package com.jarvis.design_system.button;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.jarvis.design_system.R;

public class JxToggleButton extends AppCompatToggleButton {
    public JxToggleButton(@NonNull Context context) {
        super(context);
        this.init(context);
    }

    public JxToggleButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public JxToggleButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context) {
        this.setBackgroundResource(R.drawable.selector_toggle_button);
        this.setTextOff("");
        this.setTextOn("");
        this.setText("");
    }

}
