package com.jarvis.design_system.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jarvis.design_system.R;
import com.jarvis.design_system.databinding.ViewChooseIntervalBinding;
import com.jarvis.design_system.font.TypeFaceProvider;

import java.util.Arrays;
import java.util.List;

public class CustomViewChooseInterval extends ConstraintLayout {
    private ViewChooseIntervalBinding binding;
    private Context context;
    private OnListenerCLick onListenerCLick;
    private List<String> fonts = Arrays.asList("fonts/GoogleSans-Regular.otf", "fonts/GoogleSans-Medium.otf", "fonts/GoogleSans-Bold.otf");

    public CustomViewChooseInterval(@NonNull Context context) {
        super(context);
    }

    public CustomViewChooseInterval(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewChooseInterval(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomViewChooseInterval(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.binding = ViewChooseIntervalBinding.inflate(layoutInflater, this, true);
        this.binding.tvView1.setOnClickListener(v -> {
            setColorViewLeft();
            if (onListenerCLick != null) {
                onListenerCLick.listenerView1();
            }
        });
        this.binding.tvView2.setOnClickListener(v -> {
            setColorViewView2();
            if (onListenerCLick != null) {
                onListenerCLick.listenerView2();
            }
        });

        this.binding.tvView4.setOnClickListener(v -> {
            setColorViewRight();
            if (onListenerCLick != null) {
                onListenerCLick.listenerView3();
            }
        });
    }

    private void clearColorLine() {
        this.binding.viewLinePri1.setBackgroundColor(context.getResources().getColor(R.color.ink_2));
        this.binding.viewLinePri2.setBackgroundColor(context.getResources().getColor(R.color.ink_2));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void clearColorButton() {
        this.binding.tvView1.setBackground(context.getResources().getDrawable(R.drawable.shape_radius_left_un_select));
        this.binding.tvView2.setBackground(context.getResources().getDrawable(R.drawable.shape_normal));
        this.binding.tvView4.setBackground(context.getResources().getDrawable(R.drawable.shape_radius_right_un_select));
    }

    private void clearColorTextButton() {
        Typeface typeface = TypeFaceProvider.getTypeFace(context, fonts.get(0));
        this.binding.tvView1.setTextColor(context.getResources().getColor(R.color.ink_3));
        this.binding.tvView1.setTypeface(typeface);
        this.binding.tvView2.setTextColor(context.getResources().getColor(R.color.ink_3));
        this.binding.tvView2.setTypeface(typeface);
        this.binding.tvView4.setTextColor(context.getResources().getColor(R.color.ink_3));
        this.binding.tvView4.setTypeface(typeface);
    }

    private void setColoLineLeftRight(View view) {
        clearColorLine();
        view.setBackgroundColor(context.getResources().getColor(R.color.pri_4));
    }

    private void setColoLineNormal(View view1, View view2) {
        clearColorLine();
        view1.setBackgroundColor(context.getResources().getColor(R.color.pri_4));
        view2.setBackgroundColor(context.getResources().getColor(R.color.pri_4));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setColorButtonLeft() {
        clearColorButton();
        clearColorTextButton();
        Typeface typeface = TypeFaceProvider.getTypeFace(context, fonts.get(1));
        this.binding.tvView1.setTypeface(typeface);
        this.binding.tvView1.setBackground(context.getResources().getDrawable(R.drawable.shape_radius_left_select));
        this.binding.tvView1.setTextColor(context.getResources().getColor(R.color.pri_4));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setColorButtonRight() {
        clearColorButton();
        clearColorTextButton();
        Typeface typeface = TypeFaceProvider.getTypeFace(context, fonts.get(1));
        this.binding.tvView4.setTypeface(typeface);
        this.binding.tvView4.setBackground(context.getResources().getDrawable(R.drawable.shape_radius_right_select));
        this.binding.tvView4.setTextColor(context.getResources().getColor(R.color.pri_4));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setColorButtonNormal(TextView textView) {
        clearColorButton();
        clearColorTextButton();
        Typeface typeface = TypeFaceProvider.getTypeFace(context, fonts.get(1));
        textView.setTypeface(typeface);
        textView.setTextColor(context.getResources().getColor(R.color.pri_4));
        textView.setBackground(context.getResources().getDrawable(R.drawable.shape_select_normal));
    }

    private void setColorViewLeft() {
        setColoLineLeftRight(this.binding.viewLinePri1);
        setColorButtonLeft();
    }

    private void setColorViewRight() {
        setColoLineLeftRight(this.binding.viewLinePri2);
        setColorButtonRight();
    }

    private void setColorViewView2() {
        setColoLineNormal(this.binding.viewLinePri1, this.binding.viewLinePri2);
        setColorButtonNormal(this.binding.tvView2);
    }

    public void setTextView1(String value) {
        this.binding.tvView1.setText(value);
    }

    public void setTextView2(String value) {
        this.binding.tvView2.setText(value);
    }

    public void setTextView3(String value) {
        this.binding.tvView4.setText(value);
    }

    public void setOnListenerCLick(OnListenerCLick onListenerCLick) {
        this.onListenerCLick = onListenerCLick;
    }

    public interface OnListenerCLick {
        void listenerView1();

        void listenerView2();

        void listenerView3();

    }
}
