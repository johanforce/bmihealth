package com.jarvis.design_system.forminput;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jarvis.design_system.R;
import com.jarvis.design_system.textview.CustomTextView;

public class JxPickerIcon extends CustomTextView implements JxPicker {

    private OnStateChangedListener listener;
    protected TypedArray attributeArray;
    protected Drawable itemRightIcon;

    public JxPickerIcon(@NonNull Context context) {
        super(context);
        this.init(context, null);
    }

    public JxPickerIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public JxPickerIcon(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    public String getValue() {
        return this.getText().toString();
    }

    public void setValue(String value) {
        this.setText(value);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context, @Nullable AttributeSet attrs) {
        this.setOnTouchListener((v, event) -> {
//            onNormalState();
            return false;
        });

        if (attrs == null) {
            return;
        }
        try {
            this.attributeArray = context.obtainStyledAttributes(attrs, R.styleable.JxPickerIcon);
            this.itemRightIcon = attributeArray.getDrawable(R.styleable.JxPickerIcon_itemPickerIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, itemRightIcon, null);
    }

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isEmptyContent() {
        return TextUtils.isEmpty(this.getText().toString().trim());
    }

    @Override
    public void onNormalState() {
        this.setBackgroundResource(R.drawable.selector_picker);
        if(listener != null) {
            this.listener.onNormal();
        }
    }

    @Override
    public void onErrorState() {
        this.setBackgroundResource(R.drawable.shape_text_field_error);
        if(listener != null) {
            this.listener.onError();
        }
    }
}
