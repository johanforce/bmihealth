package com.jarvis.design_system.forminput;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.jarvis.design_system.R;


public class JxPickerText extends RelativeLayout implements JxPicker {

    private TextView tvValue, tvEndText;
    private OnStateChangedListener listener;

    public JxPickerText(@NonNull Context context) {
        super(context);
        this.initView();
    }

    public JxPickerText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView();
        this.initStyle(context, attrs);
    }

    public JxPickerText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView();
        this.initStyle(context, attrs);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_picker_text, this);
        this.tvValue = inflate.findViewById(R.id.tvValue);
        this.tvEndText = inflate.findViewById(R.id.tvEndText);
        this.setOnTouchListener((View v, MotionEvent event) -> {
//            onNormalState();
            return false;
        });
    }

    private void initStyle(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.JxPickerText);
        String endText = attributeArray.getString(R.styleable.JxPickerText_endText);
        this.tvEndText.setText(endText);
    }

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.listener = listener;
    }

    public void setEndText(String endText) {
        this.tvEndText.setText(endText);
    }

    public String getValue() {
        return this.tvValue.getText().toString();
    }

    public void setValue(String value) {
        this.tvValue.setText(value);
    }

    @Override
    public boolean isEmptyContent() {
        return TextUtils.isEmpty(getValue());
    }

    @Override
    public void onNormalState() {
        this.setBackgroundResource(R.drawable.selector_picker);
        this.tvValue.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        this.tvEndText.setVisibility(VISIBLE);
        if(listener != null) {
            this.listener.onNormal();
        }
    }

    @Override
    public void onErrorState() {
        this.setBackgroundResource(R.drawable.shape_text_field_error);
        this.tvValue.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_error_empty), null);
        this.tvEndText.setVisibility(GONE);
        if(listener != null) {
            this.listener.onError();
        }
    }
}
