package com.jarvis.heathcarebmi.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.jarvis.heathcarebmi.utils.HealthIndexUtils;
import com.well.heathcarebmi.R;

public class BMICircleView extends View {
    private final String TAG = "BMIView";
    private final float TRIANGLE_DIALOG_CONST = 50f;
    private final float ADULT_VALUE_OF_SPACE_CONST = 0.3f;
    private final float CHILD_VALUE_OF_SPACE_CONST = 1.2f;
    private final float MARGIN_TOP_TRIANGLE_VS_CIRCLE_CONST = 10;
    private final float CIRCLE_RADIUS_CONST = 437.5f;
    private final float ANGLE_DIFF_LAST_BLOCK = 3f;

    //Bắt đầu các giá trị được phép thay đổi
    private String TRIANGLE_COLOR = "#515861"; //Màu tam giác chỉ giá trị
    private String[] ADULT_BGCOLOR = new String[]{"#6BD3FF", "#68A5F8", "#4ACB86", "#FFCB08", "#FF974B", "#EB4545"}; //Được phép đổi màu
    private String[] CHILD_BGCOLOR = new String[]{"#6BD3FF", "#68A5F8", "#4ACB86", "#FFCB08", "#EB4545"}; //Được phép đổi màu
    //Kết thúc các giá trị được phép thay đổi

    private float triangleDiagonal = TRIANGLE_DIALOG_CONST;
    private float marginTopTriangleVsCircle = MARGIN_TOP_TRIANGLE_VS_CIRCLE_CONST;
    private float angleDiffLastBlock = ANGLE_DIFF_LAST_BLOCK;
    private final float TRIANGLE_TOP_ANGLE = 8;

    private final float CIRCLE_WIDTH = 20;
    private final float OFFSET_ANGLE = 88;

    private final float[] ADULT_RANGES = new float[]{15.0f, 16.0f, 18.5f, 25.0f, 30.0f, 35.0f, 40.0f};
    private float adultValueOfSpace = ADULT_VALUE_OF_SPACE_CONST;

    private final float[] CHILD_RANGES = new float[]{0.0f, 3.0f, 15.0f, 85.0f, 97.0f, 100.0f};
    private float childValueOfSpace = CHILD_VALUE_OF_SPACE_CONST;

    private Paint circlePaint, trianglePaint;
    private TypedArray attributeArray;
    /**
     * Bán kính đường tròn sẽ được tính dựa theo width và height của view
     * Nếu width < height => Radius = Width / 2
     * Nếu height < width => Radius = Height / 2
     */
    private float circleRadius;
    private float parentViewWidth, parentViewHeight;
    private float marginHorizontal = 0;
    private float marginVertical = 0;
    private float triangleBottomWidth, triangleHeight;

    private float currentBmi = 102f;
    //    private long birthdayInMilliSeconds = 809024400000L;
    private long birthdayInMilliSeconds = 1471798800000L;

    public BMICircleView(Context context) {
        super(context);
        init(null, context);
    }

    public BMICircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public BMICircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        this.circlePaint = new Paint();
        this.circlePaint.setStyle(Paint.Style.STROKE);
        this.circlePaint.setStrokeWidth(CIRCLE_WIDTH);
        this.circlePaint.setStrokeCap(Paint.Cap.SQUARE);
        this.circlePaint.setAntiAlias(true);

        try {
            this.attributeArray = context.obtainStyledAttributes(attrs, R.styleable.BMICircleView);
            this.TRIANGLE_COLOR = this.attributeArray.getString(R.styleable.BMICircleView_triangle_color);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.initColorChild();
        this.initColorAdult();
        this.trianglePaint = new Paint();
        this.trianglePaint.setStyle(Paint.Style.FILL);
        if (this.TRIANGLE_COLOR == null || TRIANGLE_COLOR.equals("") || TextUtils.isEmpty(TRIANGLE_COLOR)){
            this.TRIANGLE_COLOR = "#515861";
        }
        this.trianglePaint.setColor(Color.parseColor(TRIANGLE_COLOR));
        this.trianglePaint.setAntiAlias(true);
        CornerPathEffect corEffect = new CornerPathEffect(5);
        this.trianglePaint.setPathEffect(corEffect);
    }

    private void initColorChild(){
        String child_bg_1 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_child_1));
        String child_bg_2 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_child_2));
        String child_bg_3 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_child_3));
        String child_bg_4 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_child_4));
        String child_bg_5 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_child_5));
        this.CHILD_BGCOLOR = new String[]{child_bg_1, child_bg_2, child_bg_3, child_bg_4, child_bg_5};
    }

    private void initColorAdult(){
        String adult_bg_1 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_adult_1));
        String adult_bg_2 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_adult_2));
        String adult_bg_3 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_adult_3));
        String adult_bg_4 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_adult_4));
        String adult_bg_5 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_adult_5));
        String adult_bg_6 = "#" + Integer.toHexString(ContextCompat.getColor(getContext(), R.color.bmi_circleview_bg_adult_6));
        this.ADULT_BGCOLOR = new String[]{adult_bg_1, adult_bg_2, adult_bg_3, adult_bg_4, adult_bg_5, adult_bg_6};
    }
    public void setBMIValue(long birthdayInMilliSeconds, double bmiValue) {
        this.birthdayInMilliSeconds = birthdayInMilliSeconds;
        this.currentBmi = HealthIndexUtils.roundBmiValueToFloat(bmiValue);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.parentViewWidth = getMeasuredWidth();
        this.parentViewHeight = getMeasuredHeight();

        if (parentViewHeight < parentViewWidth) {
            this.circleRadius = this.parentViewHeight / 2 - CIRCLE_WIDTH / 2;
        } else {
            this.circleRadius = this.parentViewWidth / 2 - CIRCLE_WIDTH / 2;
        }
        this.childValueOfSpace = (CIRCLE_RADIUS_CONST / circleRadius) * CHILD_VALUE_OF_SPACE_CONST;
        this.adultValueOfSpace = (CIRCLE_RADIUS_CONST / circleRadius) * ADULT_VALUE_OF_SPACE_CONST;
        this.triangleDiagonal = (TRIANGLE_DIALOG_CONST * circleRadius) / CIRCLE_RADIUS_CONST;
        this.marginTopTriangleVsCircle = (MARGIN_TOP_TRIANGLE_VS_CIRCLE_CONST * circleRadius) / CIRCLE_RADIUS_CONST;
        this.angleDiffLastBlock = (CIRCLE_RADIUS_CONST / circleRadius) * ANGLE_DIFF_LAST_BLOCK;

        this.marginVertical = this.parentViewHeight / 2 - circleRadius;
        this.marginHorizontal = this.parentViewWidth / 2 - circleRadius;

        this.triangleBottomWidth = (float) (Math.sin(Math.toRadians(TRIANGLE_TOP_ANGLE / 2)) * triangleDiagonal * 2);
        this.triangleHeight = (float) (Math.cos(Math.toRadians(TRIANGLE_TOP_ANGLE / 2)) * triangleDiagonal);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.drawCircle(canvas);
        this.drawTriangle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        float left = marginHorizontal;
        float right = parentViewWidth - marginHorizontal;
        float top = marginVertical;
        float bottom = parentViewHeight - marginVertical;

        int numberOfArc = getNumberOfArc();
        for (int i = 0; i < numberOfArc; i++) {
            float startAngle = calculateStartAngleOfArc(i);
            float sweepAngle = calculateEndAngleOfArc(i + 1) - startAngle;
            circlePaint.setColor(Color.parseColor(getBgColors()[i]));
            canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, false, circlePaint);
        }
    }

    private void drawTriangle(Canvas canvas) {
        float xTopPoint, yTopPoint;
        float xLeftBottomPoint, yLeftBottomPoint;
        float xRightBottomPoint, yRightBottomPoint;

        float alphaSweepAngle = calculateSweepAngleOfValue(currentBmi) + OFFSET_ANGLE;
        float circleRadiusToTopTriangle = circleRadius - marginTopTriangleVsCircle - CIRCLE_WIDTH / 2;

        xTopPoint = (float) Math.sin(Math.toRadians(alphaSweepAngle)) * circleRadiusToTopTriangle;
        yTopPoint = -(float) Math.cos(Math.toRadians(alphaSweepAngle)) * circleRadiusToTopTriangle;

        float circleRadiusToBottomTriangle = circleRadiusToTopTriangle - triangleHeight;
        float diagonalToBottomPoint = (float) Math.sqrt(Math.pow(circleRadiusToBottomTriangle, 2) + Math.pow(triangleBottomWidth / 2, 2));

        if (alphaSweepAngle <= 90) {

            float betaAngle = alphaSweepAngle + TRIANGLE_TOP_ANGLE / 2;
            xLeftBottomPoint = (float) Math.sin(Math.toRadians(betaAngle)) * diagonalToBottomPoint;
            yLeftBottomPoint = -(float) Math.cos(Math.toRadians(betaAngle)) * diagonalToBottomPoint;

            float gammaAngle = alphaSweepAngle - TRIANGLE_TOP_ANGLE / 2;
            xRightBottomPoint = (float) (Math.sin(Math.toRadians(gammaAngle)) * diagonalToBottomPoint);
            yRightBottomPoint = -(float) (Math.cos(Math.toRadians(gammaAngle)) * diagonalToBottomPoint);

        } else if (alphaSweepAngle <= 180) {

            float betaAngle = alphaSweepAngle - TRIANGLE_TOP_ANGLE / 2;
            xLeftBottomPoint = (float) Math.sin(Math.toRadians(betaAngle)) * diagonalToBottomPoint;
            yLeftBottomPoint = -(float) Math.cos(Math.toRadians(betaAngle)) * diagonalToBottomPoint;

            float gammaAngle = alphaSweepAngle + TRIANGLE_TOP_ANGLE / 2;
            xRightBottomPoint = (float) (Math.sin(Math.toRadians(gammaAngle)) * diagonalToBottomPoint);
            yRightBottomPoint = -(float) (Math.cos(Math.toRadians(gammaAngle)) * diagonalToBottomPoint);

        } else if (alphaSweepAngle <= 270) {

            float betaAngle = alphaSweepAngle - TRIANGLE_TOP_ANGLE / 2;
            xLeftBottomPoint = (float) Math.sin(Math.toRadians(betaAngle)) * diagonalToBottomPoint;
            yLeftBottomPoint = -(float) Math.cos(Math.toRadians(betaAngle)) * diagonalToBottomPoint;

            float gammaAngle = alphaSweepAngle + TRIANGLE_TOP_ANGLE / 2;
            xRightBottomPoint = (float) (Math.sin(Math.toRadians(gammaAngle)) * diagonalToBottomPoint);
            yRightBottomPoint = -(float) (Math.cos(Math.toRadians(gammaAngle)) * diagonalToBottomPoint);

        } else {

            float betaAngle = alphaSweepAngle - TRIANGLE_TOP_ANGLE / 2;
            xLeftBottomPoint = (float) Math.sin(Math.toRadians(betaAngle)) * diagonalToBottomPoint;
            yLeftBottomPoint = -(float) Math.cos(Math.toRadians(betaAngle)) * diagonalToBottomPoint;

            float gammaAngle = alphaSweepAngle + TRIANGLE_TOP_ANGLE / 2;
            xRightBottomPoint = (float) (Math.sin(Math.toRadians(gammaAngle)) * diagonalToBottomPoint);
            yRightBottomPoint = -(float) (Math.cos(Math.toRadians(gammaAngle)) * diagonalToBottomPoint);

        }
        xTopPoint += marginHorizontal + circleRadius;
        yTopPoint += marginVertical + circleRadius;

        xLeftBottomPoint += marginHorizontal + circleRadius;
        yLeftBottomPoint += marginVertical + circleRadius;

        xRightBottomPoint += marginHorizontal + circleRadius;
        yRightBottomPoint += marginVertical + circleRadius;

        Path path = new Path();
        path.moveTo(xTopPoint, yTopPoint);
        path.lineTo(xLeftBottomPoint, yLeftBottomPoint);
        path.lineTo(xRightBottomPoint, yRightBottomPoint);
        path.lineTo(xTopPoint, yTopPoint);
        path.close();
        canvas.drawPath(path, trianglePaint);
    }

    private int getNumberOfArc() {
        return getRangeValue().length - 1;
    }

    private float getValueInRange(int indexInRange) {
        if (indexInRange < 0 || indexInRange >= getRangeValue().length) {
            return 0;
        }
        return getRangeValue()[indexInRange];
    }

    private float getValueOfCircleLength() {
        int totalItem = getRangeValue().length;
        return (getRangeValue()[totalItem - 1] - getRangeValue()[0]) + getValueOfSpace() * getNumberOfArc();
    }

    private float calculateStartAngleOfArc(int indexInRange) {
        int numberOfArc = getNumberOfArc();
        if (indexInRange <= 0 || indexInRange > numberOfArc) {
            return getSweepAngle(0);
        }
        float firstValueInRange = getValueInRange(0);
        float valueInRange = getValueInRange(indexInRange) + indexInRange * getValueOfSpace();
        float arcLength = valueInRange - firstValueInRange;
        return getSweepAngle(arcLength);
    }

    private float calculateEndAngleOfArc(int indexInRange) {
        int numberOfArc = getNumberOfArc();
        if (indexInRange <= 0 || indexInRange > numberOfArc) {
            return getSweepAngle(0);
        }
        float firstValueInRange = getValueInRange(0);
        float valueInRange = getValueInRange(indexInRange) + (indexInRange - 1) * getValueOfSpace();
        float arcLength = valueInRange - firstValueInRange;
        return getSweepAngle(arcLength);
    }

    private float calculateSweepAngleOfValue(double value) {
        if (value <= getRangeValue()[0]) {
            return getSweepAngle(0);
        }

        int numberOfArc = getNumberOfArc();
        if (value >= getRangeValue()[numberOfArc]) {
            return getSweepAngle(getValueOfCircleLength() - getValueOfSpace()) + this.angleDiffLastBlock; //TODO tech debt: chưa hiểu tại sao bị lệch x độ
        }

        //Tính số lượng space chỉ tính đến block liền kề block cuối
        int totalSpace = 0;
        for (int i = 0; i < numberOfArc - 1; i++) {
            if (value <= getRangeValue()[i]) {
                break;
            }
            totalSpace++;
        }

        float arcLength = (float) ((value - getRangeValue()[0]) + totalSpace * getValueOfSpace());
        return getSweepAngle(arcLength);
    }

    private float getSweepAngle(float arcLength) {
        float sweepAngle = arcLength / getValueOfCircleLength() * 360;
        return sweepAngle - OFFSET_ANGLE;
    }

    private float[] getRangeValue() {
        return HealthIndexUtils.isChild(birthdayInMilliSeconds) ? CHILD_RANGES : ADULT_RANGES;
    }

    private String[] getBgColors() {
        return HealthIndexUtils.isChild(birthdayInMilliSeconds) ? CHILD_BGCOLOR : ADULT_BGCOLOR;
    }

    private float getValueOfSpace() {
        return HealthIndexUtils.isChild(birthdayInMilliSeconds) ? childValueOfSpace : adultValueOfSpace;
    }
}
