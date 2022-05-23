package com.jarvis.design_system.button;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.jarvis.design_system.R;

public class JxCheckBoxButton extends AppCompatCheckBox {
    public JxCheckBoxButton(@NonNull Context context) {
        super(context);
        this.init(context);
    }

    public JxCheckBoxButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public JxCheckBoxButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context) {
        this.setButtonDrawable(R.drawable.selector_checkbox);
    }
}
