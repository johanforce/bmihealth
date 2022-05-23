package com.jarvis.design_system.forminput;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.jarvis.design_system.R;

public class JxPickerIconWithLabel extends RelativeLayout implements OnStateChangedListener {

    private JxPickerIcon inputField;
    private JxTextFieldLabel textTitle;
    protected Drawable itemRightIcon;
    private OnListenerClick onListenerClick;

    public JxPickerIconWithLabel(Context context) {
        super(context);
        initView(context);
    }

    public JxPickerIconWithLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initStyle(context, attrs);
    }

    public JxPickerIconWithLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initStyle(context, attrs);
    }

    public JxPickerIconWithLabel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
        initStyle(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_picker_icon_label, this);
        this.inputField = inflate.findViewById(R.id.tvInputLabel);
        this.textTitle = inflate.findViewById(R.id.tvTitle);
        inputField.setOnStateChangedListener(this);
        inputField.setOnClickListener(v -> {
            if (onListenerClick != null) {
                onListenerClick.onClick();
            }
        });

        inputField.setOnLongClickListener(v -> {
            if (onListenerClick != null) {
                onListenerClick.onClick();
            }
            return false;
        });
    }

    public void setOnListenerClick(OnListenerClick onListenerClick) {
        this.onListenerClick = onListenerClick;
    }

    public String getValue() {
        return inputField.getText().toString();
    }

    public boolean isEmptyContent() {
        return TextUtils.isEmpty(this.inputField.getText().toString().trim());
    }

    private void initStyle(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.JxPickerIconWithLabel);
        String title = attributeArray.getString(R.styleable.JxPickerIconWithLabel_title1);
        String hint = attributeArray.getString(R.styleable.JxPickerIconWithLabel_hint1);
        this.itemRightIcon = attributeArray.getDrawable(R.styleable.JxPickerIconWithLabel_itemPickerIcon1);

        this.textTitle.setText(title);
        inputField.setHint(hint);
        inputField.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, itemRightIcon, null);
    }

    public void onNormalState() {
        this.inputField.onNormalState();
        this.textTitle.onNormalState();
    }

    public void onErrorState() {
        this.inputField.onErrorState();
        this.textTitle.onErrorState();
    }

    @Override
    public void onTyping() {
        this.textTitle.onTypingState();
    }

    @Override
    public void onNormal() {
        this.textTitle.onNormalState();
    }

    @Override
    public void onError() {
        this.textTitle.onErrorState();
    }

    public void setValue(String value) {
        inputField.setValue(value);
    }

    public interface OnListenerClick {
        void onClick();
    }

    public JxPickerIcon getInputField() {
        return inputField;
    }

    public JxTextFieldLabel getTextTitle() {
        return textTitle;
    }
}
