package com.jarvis.design_system.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;

import com.jarvis.design_system.R;
import com.jarvis.design_system.font.TypeFaceProvider;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jarvis Nguyen on 20/01/2022.
 */

public class CustomTextView extends AppCompatTextView {

    private List<String> fonts = Arrays.asList("fonts/GoogleSans-Regular.otf", "fonts/GoogleSans-Medium.otf", "fonts/GoogleSans-Bold.otf", "fonts/Sportypo-Reguler.ttf");

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        try {
            TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.JxCustomTextView);
            int typeface_index = attributeArray.getInt(R.styleable.JxCustomTextView_textFont, 0);
            boolean isSelected = attributeArray.getBoolean(R.styleable.JxCustomTextView_selected, false);

            Drawable drawableStart = null;
            Drawable drawableEnd = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawableStart = attributeArray.getDrawable(R.styleable.JxCustomTextView_drawableStartCompat);
                drawableEnd = attributeArray.getDrawable(R.styleable.JxCustomTextView_drawableEndCompat);
                drawableBottom = attributeArray.getDrawable(R.styleable.JxCustomTextView_drawableBottomCompat);
                drawableTop = attributeArray.getDrawable(R.styleable.JxCustomTextView_drawableTopCompat);
            } else {
                final int drawableStartId = attributeArray.getResourceId(R.styleable.JxCustomTextView_drawableStartCompat, -1);
                final int drawableEndId = attributeArray.getResourceId(R.styleable.JxCustomTextView_drawableEndCompat, -1);
                final int drawableBottomId = attributeArray.getResourceId(R.styleable.JxCustomTextView_drawableBottomCompat, -1);
                final int drawableTopId = attributeArray.getResourceId(R.styleable.JxCustomTextView_drawableTopCompat, -1);

                if (drawableStartId != -1)
                    drawableStart = AppCompatResources.getDrawable(context, drawableStartId);
                if (drawableEndId != -1)
                    drawableEnd = AppCompatResources.getDrawable(context, drawableEndId);
                if (drawableBottomId != -1)
                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
                if (drawableTopId != -1)
                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
            }

            // to support rtl
            setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart, drawableTop, drawableEnd, drawableBottom);

            setSelected(isSelected);
            Typeface typeface = TypeFaceProvider.getTypeFace(context, fonts.get(typeface_index));

            super.setTypeface(typeface);

            attributeArray.recycle();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
