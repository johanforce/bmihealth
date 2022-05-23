package com.jarvis.design_system.forminput;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import com.jarvis.design_system.R;
import com.jarvis.design_system.textview.CustomTextView;

public class JxInputTextFieldWithLabel extends RelativeLayout implements OnStateChangedListener {

    private JxTextField inputField;
    private JxTextFieldLabel textTitle;
    private CustomTextView textDes;
    private int titleVisibility;

    public JxInputTextFieldWithLabel(Context context) {
        super(context);
        initView(context);
    }

    public JxInputTextFieldWithLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initStyle(context, attrs);
    }

    public JxInputTextFieldWithLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initStyle(context, attrs);
    }

    public JxInputTextFieldWithLabel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
        initStyle(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_input_text_label, this);
        this.inputField = inflate.findViewById(R.id.tvInputLabel);
        this.textTitle = inflate.findViewById(R.id.tvTitle);
        this.textDes = inflate.findViewById(R.id.tvDes);
        inputField.setOnStateChangedListener(this);
    }

    private void initStyle(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        @SuppressLint({"CustomViewStyleable", "Recycle"}) TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.JxInputTextField);
        String title = attributeArray.getString(R.styleable.JxInputTextField_title);
        String des = attributeArray.getString(R.styleable.JxInputTextField_des);
        String hint = attributeArray.getString(R.styleable.JxInputTextField_hint);
        this.titleVisibility = attributeArray.getInt(R.styleable.JxInputTextField_titleVisibility, VISIBLE);
        int inputType = attributeArray.getInt(R.styleable.JxInputTextField_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL);
        int imeOption = attributeArray.getInt(R.styleable.JxInputTextField_android_imeOptions, EditorInfo.TYPE_TEXT_VARIATION_NORMAL);
        boolean enableErrorState = attributeArray.getBoolean(R.styleable.JxInputTextField_enableErrorState, true);
        boolean isSingleLine = attributeArray.getBoolean(R.styleable.JxInputTextField_singleLine, false);

        this.textTitle.setVisibility(titleVisibility);
        this.textTitle.setText(title);
        this.inputField.setInputType(inputType);
        this.inputField.setImeOptions(imeOption);
        this.inputField.setEnableErrorState(enableErrorState);
        this.inputField.setSingleLine(isSingleLine);
        this.inputField.setMaxLines(4);
        if (!TextUtils.isEmpty(hint)) {
            this.inputField.setHint(hint);
        }
        if (!TextUtils.isEmpty(des)) {
            this.textDes.setVisibility(VISIBLE);
            this.textDes.setText(des);
        } else {
            this.textDes.setVisibility(GONE);
        }
    }

    public void onNormalState() {
        this.inputField.onNormalState();
        this.textTitle.onNormalState();
        this.textTitle.setVisibility(titleVisibility);
    }

    public void onTypingState() {
        this.inputField.onTypingState();
        this.textTitle.onTypingState();
        this.textTitle.setVisibility(titleVisibility);
    }

    public void onErrorState() {
        this.inputField.onErrorState();
        this.textTitle.onErrorState();
        this.textTitle.setVisibility(titleVisibility);
    }

    public void onDisableState() {
        this.inputField.onDisableState();
        this.textTitle.onDisableState();
        this.textTitle.setVisibility(titleVisibility);
    }

    public void setEnableErrorState(boolean isEnableErrorState) {
        this.inputField.setEnableErrorState(isEnableErrorState);
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

    public void setSelection() {
        inputField.setSelection(inputField.getContent().length());
    }

    public void setHintValue(String hint) {
        inputField.setHint(hint);
    }

    public void setHintColor(int color) {
        inputField.setHintTextColor(color);
    }

    public JxTextField getInputField() {
        return inputField;
    }

    public JxTextFieldLabel getTextTitle() {
        return textTitle;
    }
}
