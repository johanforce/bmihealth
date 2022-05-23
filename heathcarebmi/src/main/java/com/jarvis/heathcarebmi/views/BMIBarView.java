package com.jarvis.heathcarebmi.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.jarvis.heathcarebmi.utils.Consts;
import com.jarvis.heathcarebmi.utils.HealthIndexUtils;
import com.well.heathcarebmi.R;


public class BMIBarView extends View {

    //=> Range Value
    private final float[] ADULT_RANGES = new float[]{15.0f, 16.0f, 18.5f, 25.0f, 30.0f, 35.0f, 40.0f};
    private final String[] ADULT_LABELS = new String[]{"15", "16", "18.5", "25", "30", "35", "40"};
    private final int[] ADULT_TYPE_RESOURCE = new int[]{R.string.very_underweight, R.string.underweight, R.string.healthy_weight, R.string.overweight, R.string.moderately_obese, R.string.very_obese};
    //<editor-fold desc="Value for Child">
    private final int CHILD_TOTALBLOCK = 5;
    //</editor-fold>
    private final String TRANSPARENT_COLOR = "#00000000";
    //<editor-fold desc="Value for Adult">
    private int ADULT_TOTALBLOCK = 6;
    //=> Range Value
    private float[] CHILD_RANGES = new float[]{0.0f, 3.0f, 15.0f, 85.0f, 97.0f, 100.0f};
    private String[] CHILD_LABELS = new String[]{"", "3rd", "15th", "85th", "97th", ""};
    //</editor-fold>
    private int[] CHILD_TYPE_RESOURCE = new int[]{R.string.very_underweight, R.string.underweight, R.string.healthy_weight, R.string.overweight, R.string.obese};
    private String XCOORD_COLOR = "#3B3B3B";
    private String UNIT_COLOR = "#FFFFFF";
    private String BACKGROUND_COLOR = "#FFFFFF";
    private String RULER_COLOR = "#000000";

    private float f16419c;
    private String[] types;
    private float[] range;
    private String[] labels;
    private int totalBlock;
    private float[] listBarBlockAxisX;
    private float unitTextSize;
    private String unitTextColor;
    private Paint xCoordinatePaint;
    private float xCoordSize;
    private String xCoordColor;
    private Paint stateTextPaint;
    private float stateTextSize;
    private Paint ruleTextPaint;
    private float rulerValueTextSize;
    private float rulerWidth;
    private float rulerOffsetHeight;
    private String rulerColor;
    private String bgColor;
    private float f16417a;
    private float f16418b;
    private int measureWidth;
    private float bmiViewHeight;
    private int type;
    private String[] bmiColors;
    private String[] bmiColorsGra;
    private float f16436t;
    private float blankSpacingWidth;
    private float blankPercent;
    private float bmiValue;
    private float barHeight;
    private Paint textPaint;
    private Context context;
    private TypedArray attributeArray;
    private OnBmiValueChangedListener bmiValueChangedListener;
    private boolean isAdultView;

    public BMIBarView(Context context) {
        super(context);
        init(null, context);
    }

    public BMIBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, context);
    }

    private void init(AttributeSet attrs, Context context) {
        this.context = context;
        this.f16417a = 0.0f;
        this.f16419c = 0.0f;
        this.type = 0;
        this.blankPercent = 0.009f;
        this.bmiValue = 0.0f;
        this.barHeight = context.getResources().getDimension(R.dimen.px56);
        this.unitTextColor = "";
        this.xCoordColor = "";
        this.rulerColor = "";
        try {
            this.attributeArray = context.obtainStyledAttributes(attrs, R.styleable.BmiView);
            this.BACKGROUND_COLOR = this.attributeArray.getString(R.styleable.BmiView_background_color);
            this.XCOORD_COLOR = this.attributeArray.getString(R.styleable.BmiView_text_color);
            this.RULER_COLOR = this.attributeArray.getString(R.styleable.BmiView_ruler_color);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.bgColor = BACKGROUND_COLOR;
        this.UNIT_COLOR = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_unit));
        this.initValueWithAdultAge();
        setClickable(true);
        setFocusable(true);
    }

    private void initValueWithAdultAge() {
        this.isAdultView = true;
        this.totalBlock = ADULT_TOTALBLOCK;
        this.types = new String[this.totalBlock];
        this.range = ADULT_RANGES;
        String adult_label1 = getContext().getString(R.string.bmi_barview_adult_label1);
        String adult_label2 = getContext().getString(R.string.bmi_barview_adult_label2);
        String adult_label3 = getContext().getString(R.string.bmi_barview_adult_label3);
        String adult_label4 = getContext().getString(R.string.bmi_barview_adult_label4);
        String adult_label5 = getContext().getString(R.string.bmi_barview_adult_label5);
        String adult_label6 = getContext().getString(R.string.bmi_barview_adult_label6);
        String adult_label7 = getContext().getString(R.string.bmi_barview_adult_label7);
        this.labels = new String[]{adult_label1, adult_label2, adult_label3, adult_label4, adult_label5, adult_label6, adult_label7};

        this.listBarBlockAxisX = new float[this.totalBlock * 2 + 1];
        String adult_bg_start1 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_start_1));
        String adult_bg_start2 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_start_2));
        String adult_bg_start3 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_start_3));
        String adult_bg_start4 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_start_4));
        String adult_bg_start5 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_start_5));
        String adult_bg_start6 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_start_6));
        bmiColors = new String[]{adult_bg_start1, adult_bg_start2, adult_bg_start3, adult_bg_start4, adult_bg_start5, adult_bg_start6};

        String adult_bg_end1 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_end_1));
        String adult_bg_end2 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_end_2));
        String adult_bg_end3 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_end_3));
        String adult_bg_end4 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_end_4));
        String adult_bg_end5 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_end_5));
        String adult_bg_end6 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_adult_end_6));
        this.bmiColorsGra = new String[]{adult_bg_end1, adult_bg_end2, adult_bg_end3, adult_bg_end4, adult_bg_end5, adult_bg_end6};
        for (int i = 0; i < this.totalBlock; i++) {
            this.types[i] = context.getString(ADULT_TYPE_RESOURCE[i]);
        }
    }

    private void initValeWithChildAge() {
        this.isAdultView = false;
        this.totalBlock = CHILD_TOTALBLOCK;
        this.types = new String[this.totalBlock];
        this.range = CHILD_RANGES;
        String child_label1 = getContext().getString(R.string.bmi_barview_child_label1);
        String child_label2 = getContext().getString(R.string.bmi_barview_child_label2);
        String child_label3 = getContext().getString(R.string.bmi_barview_child_label3);
        String child_label4 = getContext().getString(R.string.bmi_barview_child_label4);
        String child_label5 = getContext().getString(R.string.bmi_barview_child_label5);
        String child_label6 = getContext().getString(R.string.bmi_barview_child_label6);
        this.labels = new String[]{child_label1, child_label2, child_label3, child_label4, child_label5, child_label6};
        this.listBarBlockAxisX = new float[this.totalBlock * 2 + 1];
        String child_bg_start1 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_start_1));
        String child_bg_start2 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_start_2));
        String child_bg_start3 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_start_3));
        String child_bg_start4 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_start_4));
        String child_bg_start5 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_start_5));
        this.bmiColors = new String[]{child_bg_start1, child_bg_start2, child_bg_start3, child_bg_start4, child_bg_start5};

        String child_bg_end1 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_end_1));
        String child_bg_end2 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_end_2));
        String child_bg_end3 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_end_3));
        String child_bg_end4 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_end_4));
        String child_bg_end5 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_barview_bg_child_end_5));
        this.bmiColorsGra = new String[]{child_bg_end1, child_bg_end2, child_bg_end3, child_bg_end4, child_bg_end5};
        for (int i = 0; i < this.totalBlock; i++) {
            this.types[i] = context.getString(CHILD_TYPE_RESOURCE[i]);
        }
    }

    @Override
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.measureWidth = getMeasuredWidth();
        if (this.measureWidth == 0) {
            this.measureWidth = getWidth();
        }
        measureBarBlockAxisX();
        initPaint();
        setMeasuredDimension(this.measureWidth, ((int) this.bmiViewHeight) + 1);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(TRANSPARENT_COLOR));
        canvas.drawRect(0.0f, 0.0f, (float) this.measureWidth, this.bmiViewHeight, paint);
        this.f16417a = this.f16418b;
        drawBMIRangeBlock(canvas);
        drawBMIValueText(canvas);
        drawRuler(canvas);
    }

    private float getBlankPercent() {
        return this.blankPercent;
    }

    public void setBlankPercent(float blankPercent) {
        this.blankPercent = blankPercent;
    }

    private void measureBarBlockAxisX() {
        int i = 0;
        float height = this.barHeight / 425.0f;
        float blankPercent = getBlankPercent();
        float[] bmiBlockPercent = new float[totalBlock];

        int totalBlankBlocks = this.totalBlock + 1;
        double totalValueInRange = range[totalBlock] - range[0];
        for (int index = 0; index < this.totalBlock; index++) {
            float blockPercentByValue = (float) ((range[index + 1] - range[index]) / totalValueInRange);
            bmiBlockPercent[index] = (1.0f - (getBlankPercent() * totalBlankBlocks)) * blockPercentByValue;
        }

        float[] bmiBlockWidth = new float[totalBlock];
        for (int i2 = 0; i2 < bmiBlockPercent.length; i2++) {
            bmiBlockWidth[i2] = ((float) this.measureWidth) * bmiBlockPercent[i2];
        }
        this.f16436t = ((float) this.measureWidth) * height;
        this.blankSpacingWidth = ((float) this.measureWidth) * blankPercent;
        float f = blankSpacingWidth;
        while (i < totalBlock) {
            this.listBarBlockAxisX[i * 2] = f;
            this.listBarBlockAxisX[(i * 2) + 1] = bmiBlockWidth[i] + f;
            f += bmiBlockWidth[i] + this.blankSpacingWidth;
            i++;
        }
        this.listBarBlockAxisX[i * 2] = f;
    }

    private void initPaint() {
        this.f16417a = 0.0f;
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setColor(Color.parseColor(getUnitTextColor()));
        this.textPaint.setTextSize(getUnitTextSize());

        this.xCoordinatePaint = new Paint();
        this.xCoordinatePaint.setAntiAlias(true);
        this.xCoordinatePaint.setColor(Color.parseColor(getxCoordinateColor()));
        this.xCoordinatePaint.setTypeface(Typeface.DEFAULT);
        this.xCoordinatePaint.setTextSize(getxCoordinateSize());

        this.stateTextPaint = new Paint();
        this.stateTextPaint.setAntiAlias(true);
        this.stateTextPaint.setTextSize(getStateTextSize());

        this.ruleTextPaint = new Paint();
        this.ruleTextPaint.setAntiAlias(true);
        this.ruleTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        this.ruleTextPaint.setColor(Color.parseColor(getRulerColor()));
        this.ruleTextPaint.setTextSize(getRulerValueTextSize());

        this.bmiViewHeight = this.textPaint.getFontSpacing() - this.textPaint.descent();
        float descent = this.bmiViewHeight + this.textPaint.descent();
        this.bmiViewHeight += this.textPaint.descent() + this.f16436t;
        float rulerOffsetHeight = ((getRulerOffsetHeight() + this.ruleTextPaint.descent()) + this.ruleTextPaint.getFontSpacing()) - this.ruleTextPaint.descent();
        if (rulerOffsetHeight > descent) {
            this.bmiViewHeight += rulerOffsetHeight - descent;
            this.f16417a = rulerOffsetHeight - descent;
        }
        this.f16418b = this.f16417a;
    }

    private String getBmiState() {
        return this.types[this.type];
    }

    private int getIdBmiState() {
        return this.type;
    }

    private String getBmiStateColor() {
        return this.bmiColors[this.type];
    }

    public BMIBarView addBmiValueChangedListener(OnBmiValueChangedListener bmiValueChangedListener) {
        this.bmiValueChangedListener = bmiValueChangedListener;
        return this;
    }

    public float getBMIValue() {
        return this.bmiValue;
    }

    /**
     * birthday: time in milliseconds
     */
    public BMIBarView setBMIValue(long birthday, double bmiValue) {
        int ageInMonth = HealthIndexUtils.calculateAgeInMonth(birthday);
        if (ageInMonth <= Consts.MAX_CHILD_AGE_IN_MONTH) {
            if (isAdultView) {
                initValeWithChildAge();
                measureBarBlockAxisX();
            }
        } else {
            if (!isAdultView) {
                initValueWithAdultAge();
                measureBarBlockAxisX();
            }
        }
        try {
            this.bmiValue = HealthIndexUtils.roundBmiValueToFloat(bmiValue);
            boolean match = false;
            for (int i = 0; i < totalBlock; i++) {
                if (bmiValue < this.range[0]) {
                    this.type = 0;
                    match = true;
                    break;
                }
                if (bmiValue < this.range[i]) {
                    match = true;
                    this.type = i - 1;
                    break;
                }
            }
            if (!match) {
                this.type = totalBlock - 1;
            }
            if (this.bmiValueChangedListener != null) {
                this.bmiValueChangedListener.onValueChanged(bmiValue, getBmiState(), Color.parseColor(getBmiStateColor()), getIdBmiState());
            }
            postInvalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private float getUnitTextSize() {
        if (this.unitTextSize == 0.0f) {
            this.unitTextSize = context.getResources().getDimension(R.dimen.px16);
        }
        return this.unitTextSize;
    }

    public void setUnitTextSize(float unitTextSize) {
        this.unitTextSize = unitTextSize;
    }

    private String getUnitTextColor() {
        if (this.unitTextColor == null || this.unitTextColor.equals("")) {
            this.unitTextColor = UNIT_COLOR;
        }
        return this.unitTextColor;
    }

    public void setUnitTextColor(String unitTextColor) {
        this.unitTextColor = unitTextColor;
    }

    private void drawBMIRangeBlock(Canvas canvas) {
        int i = 0;
        while (true) {
            if (i <= totalBlock) {
                Paint paint = new Paint();
                if (i == totalBlock) {
                    paint.setColor(Color.TRANSPARENT);
                    canvas.drawRoundRect(this.listBarBlockAxisX[(i * 2) - 1], this.f16417a, this.listBarBlockAxisX[i * 2], this.f16436t + this.f16417a, Consts.ROUND_VALUE_LEVEL, Consts.ROUND_VALUE_LEVEL, paint);
                } else {
                    paint.setShader(new LinearGradient(this.listBarBlockAxisX[(i * 2) + 1]
                            , this.f16417a, this.listBarBlockAxisX[i * 2], this.f16436t + this.f16417a, Color.parseColor(this.bmiColors[i]), Color.parseColor(this.bmiColorsGra[i]), Shader.TileMode.REPEAT));
                    canvas.drawRoundRect(this.listBarBlockAxisX[i * 2], this.f16417a, this.listBarBlockAxisX[(i * 2) + 1], this.f16436t + this.f16417a, Consts.ROUND_VALUE_LEVEL, Consts.ROUND_VALUE_LEVEL, paint);
                }
                i++;
            } else {
                this.f16417a += this.f16436t;
                return;
            }
        }
    }

    private float getxCoordinateSize() {
        if (this.xCoordSize == 0.0f) {
            this.xCoordSize = context.getResources().getDimension(R.dimen.px8);
        }
        return this.xCoordSize;
    }

    public BMIBarView setxCoordinateSize(float xCoordSize) {
        this.xCoordSize = xCoordSize;
        return this;
    }

    private String getxCoordinateColor() {
        this.xCoordColor = XCOORD_COLOR;
        if (this.xCoordColor == null || this.xCoordColor.equals("") || TextUtils.isEmpty(this.xCoordColor)) {
            this.xCoordColor = "#3B3B3B";
        }
        return this.xCoordColor;
    }

    public BMIBarView setxCoordinateColor(String xCoordinateColor) {
        this.xCoordColor = xCoordinateColor;
        return this;
    }

    private void drawBMIValueText(Canvas canvas) {
        this.f16417a += this.xCoordinatePaint.getFontSpacing();
        for (int i = 0; i < this.bmiColors.length; i++) {
            if (i == 0) {
                this.xCoordinatePaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(this.labels[i], this.listBarBlockAxisX[i * 2] - this.blankSpacingWidth, this.f16417a, this.xCoordinatePaint);
            } else {
                this.xCoordinatePaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(this.labels[i], this.listBarBlockAxisX[i * 2] - (this.blankSpacingWidth / 2.0f), this.f16417a, this.xCoordinatePaint);
            }
        }
        this.xCoordinatePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(this.labels[this.labels.length - 1], this.listBarBlockAxisX[this.listBarBlockAxisX.length - 1], this.f16417a, this.xCoordinatePaint);
        this.f16417a += this.xCoordinatePaint.descent();
    }

    private float getStateTextSize() {
        if (this.stateTextSize == 0.0f) {
            this.stateTextSize = context.getResources().getDimension(R.dimen.px14);
        }
        return this.stateTextSize;
    }

    public void setStateTextSize(float stateTextSize) {
        this.stateTextSize = stateTextSize;
    }

    private String getRulerColor() {
        this.rulerColor = RULER_COLOR;
        if (this.rulerColor == null || this.rulerColor.equals("")) {
            this.rulerColor = "#000000";
        }
        return this.rulerColor;
    }

    public BMIBarView setRulerColor(String rulerColor) {
        this.rulerColor = rulerColor;
        return this;
    }

    private float getRulerValueTextSize() {
        if (this.rulerValueTextSize == 0.0f) {
            this.rulerValueTextSize = context.getResources().getDimension(R.dimen.px16);
        }
        return this.rulerValueTextSize;
    }

    public void setRulerValueTextSize(float rulerValueTextSize) {
        this.rulerValueTextSize = rulerValueTextSize;
    }

    private float getRulerWidth() {
        if (this.rulerWidth == 0.0f) {
            this.rulerWidth = context.getResources().getDimension(R.dimen.px2);
        }
        return this.rulerWidth;
    }

    public BMIBarView setRulerWidth(float rulerWidth) {
        this.rulerWidth = rulerWidth;
        return this;
    }

    private float getRulerOffsetHeight() {
        if (this.rulerOffsetHeight == 0.0f) {
            this.rulerOffsetHeight = context.getResources().getDimension(R.dimen.px2);
        }
        return this.rulerOffsetHeight;
    }

    public BMIBarView setRulerOffsetHeight(float rulerOffsetHeight) {
        this.rulerOffsetHeight = rulerOffsetHeight;
        return this;
    }

    private void drawRuler(Canvas canvas) {
        float xPositionLeft;
        float f2;
        if (this.bmiValue < this.range[0]) {
            xPositionLeft = 0.0f;
        } else if (this.bmiValue > this.range[this.range.length - 1]) {
            xPositionLeft = (float) this.measureWidth;
        } else {
            f2 = this.range[this.type];
            float f3 = this.range[this.type + 1];
            float f4 = this.listBarBlockAxisX[this.type * 2];
            xPositionLeft = (((this.bmiValue - f2) / (f3 - f2)) * (this.listBarBlockAxisX[(this.type * 2) + 1] - f4)) + f4;
        }
        canvas.drawRect(xPositionLeft - (getRulerWidth() / 2.0f), f16419c + getRulerOffsetHeight(), xPositionLeft + (getRulerWidth() / 2.0f), getRulerOffsetHeight() + (this.f16419c + this.f16436t), this.ruleTextPaint);
        canvas.drawCircle(xPositionLeft, this.f16419c + getRulerOffsetHeight(), getRulerWidth() / 2.0f, this.ruleTextPaint);

        canvas.drawCircle(xPositionLeft, (this.f16419c + this.f16436t) + getRulerOffsetHeight(), getRulerWidth() / 2.0f, this.ruleTextPaint);
        f2 = this.ruleTextPaint.measureText(this.bmiValue + "") / 2.0f;
        if (xPositionLeft - f2 < 0.0f) {
            this.ruleTextPaint.setTextAlign(Paint.Align.LEFT);
        } else if ((f2 + xPositionLeft) - ((float) this.measureWidth) > 0.0f) {
            this.ruleTextPaint.setTextAlign(Paint.Align.RIGHT);
        } else {
            this.ruleTextPaint.setTextAlign(Paint.Align.CENTER);
        }
    }

    private String getViewBackgroundColor() {
        this.bgColor = BACKGROUND_COLOR;
        if (this.bgColor == null || this.bgColor.equals("")) {
            this.bgColor = "#FFFFFF";
        }
        return this.bgColor;
    }

    public BMIBarView setViewBackgroundColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public BMIBarView setBarHeight(float height) {
        this.barHeight = height;
        return this;
    }

    public interface OnBmiValueChangedListener {
        void onValueChanged(double bmiValue, String bmiStateString, int bmiStateColor, int idBMIState);
    }
}