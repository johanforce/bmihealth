@file:Suppress(
    "MemberVisibilityCanBePrivate", "unused", "SameParameterValue", "DEPRECATION",
    "NAME_SHADOWING", "PrivatePropertyName"
)

package com.jarvis.bmihealth.presentation.home

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.presentation.utilx.DeviceUtil

/**
 * colorful arc progress bar
 * Created by Jarvis Nguyen on 12/4/15.
 */
class ColorArcProgressBar : View {
    private val mWidth = 0
    private val mHeight = 0
    private var diameter = 500 //直径
    private var centerX //圆心X坐标
            = 0f
    private var centerY //圆心Y坐标
            = 0f
    private var allArcPaint: Paint? = null
    private var progressPaint: Paint? = null
    private var vTextPaint: Paint? = null
    private var hintPaint: Paint? = null
    private var degreePaint: Paint? = null
    private var curSpeedPaint: Paint? = null
    private var bgRect: RectF? = null
    private var progressAnimator: ValueAnimator? = null
    private var mDrawFilter: PaintFlagsDrawFilter? = null
    private var sweepGradient: SweepGradient? = null
    private var rotateMatrix: Matrix? = null
    private val startAngle = 127f
    private var sweepAngle = 270f
    private var currentAngle = 0f
    private var lastAngle = 0f
    private var colors = intArrayOf(Color.GREEN, Color.YELLOW, Color.RED, Color.RED)
    private var maxValues = 60f
    private var curValues = 0f
    private var isChild = false
    private var bgArcWidth = dipToPx(2f).toFloat()
    private var progressWidth = dipToPx(10f).toFloat()
    private var textSize = dipToPx(60f).toFloat()
    private var hintSize = dipToPx(15f).toFloat()
    private val curSpeedSize = dipToPx(13f).toFloat()
    private val aniSpeed = 1000
    private val longdegree = dipToPx(13f).toFloat()
    private val shortdegree = dipToPx(5f).toFloat()
    private val DEGREE_PROGRESS_DISTANCE = dipToPx(8f)
    private var hintColor = context.getColor(R.color.ink_3)
    private val longDegreeColor = "#111111"
    private val shortDegreeColor = "#111111"
    private val bgArcColor = context.getColor(R.color.ink_3)
    private var titleString: String? = null
    private var hintString: String? = null
    private val isShowCurrentSpeed = true
    private var isNeedTitle = false
    private var isNeedUnit = false
    private var isNeedDial = false
    private var isNeedContent = false
    private var dataBMI = 0.0f
    private var listColorAdult = listOf(
        context.getColor(R.color.secondary),
        context.getColor(R.color.bmi_level1),
        context.getColor(R.color.bmi_level2),
        context.getColor(R.color.bmi_level3),
        context.getColor(R.color.bmi_level4),
        context.getColor(R.color.bmi_level5),
        context.getColor(R.color.bmi_level5),
    )
    private var listColorChild = listOf(
        context.getColor(R.color.secondary),
        context.getColor(R.color.bmi_level1),
        context.getColor(R.color.bmi_level2),
        context.getColor(R.color.bmi_level3),
        context.getColor(R.color.bmi_level5),
    )

    private var listStringChild = listOf(
        context.getString(R.string.bmi_very_under_weight),
        context.getString(R.string.bmi_under_weight),
        context.getString(R.string.bmi_healthy_weight),
        context.getString(R.string.bmi_over_weight),
        context.getString(R.string.bmi_obesity),
    )

    private var listStringAdult = listOf(
        context.getString(R.string.bmi_very_under_weight),
        context.getString(R.string.bmi_under_weight),
        context.getString(R.string.bmi_healthy_weight),
        context.getString(R.string.bmi_over_weight),
        context.getString(R.string.bmi_moderatelt_1),
        context.getString(R.string.bmi_moderatelt_2),
        context.getString(R.string.bmi_moderatelt_3),
    )

    // sweepAngle / maxValues 的值
    private var k = 0f

    constructor(context: Context?) : super(context, null) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        initConfig(context, attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initConfig(context, attrs)
        initView()
    }

    /**
     * 初始化布局配置
     *
     * @param context
     * @param attrs
     */
    private fun initConfig(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar)
        colors = intArrayOf(
            ContextCompat.getColor(context, R.color.bmi_level1),
            ContextCompat.getColor(context, R.color.bmi_level2),
            ContextCompat.getColor(context, R.color.bmi_level3),
            ContextCompat.getColor(context, R.color.bmi_level4),
            ContextCompat.getColor(context, R.color.bmi_level5)
        )

//        colors = new int[]{color1,
//                color2, color3};
        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270).toFloat()
        bgArcWidth =
            a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2f).toFloat())
        progressWidth =
            a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10f).toFloat())
        isNeedTitle = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, false)
        isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false)
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false)
        isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false)
        hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit)
        titleString = a.getString(R.styleable.ColorArcProgressBar_string_title)
        curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0f)
        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60f)
        setCurrentValues(curValues, isChild)
        setMaxValues(maxValues)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width =
            (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE).toInt()
        val height =
            (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE).toInt()
        setMeasuredDimension(width, height)
    }

    private fun initView() {
        diameter = 3 * screenWidth / 5
        //弧形的矩阵区域
        bgRect = RectF()
        bgRect!!.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE
        bgRect!!.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE
        bgRect!!.right = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE)
        bgRect!!.bottom = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE)

        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2

        //外部刻度线
        degreePaint = Paint()
        degreePaint!!.color = Color.parseColor(longDegreeColor)

        //整个弧形
        allArcPaint = Paint()
        allArcPaint!!.isAntiAlias = true
        allArcPaint!!.style = Paint.Style.STROKE
        allArcPaint!!.strokeWidth = bgArcWidth
        allArcPaint!!.color = bgArcColor
        allArcPaint!!.strokeCap = Paint.Cap.ROUND

        //当前进度的弧形
        progressPaint = Paint()
        progressPaint!!.isAntiAlias = true
        progressPaint!!.style = Paint.Style.STROKE
        progressPaint!!.strokeCap = Paint.Cap.ROUND
        progressPaint!!.strokeWidth = progressWidth
        progressPaint!!.color = Color.GREEN

        //内容显示文字
        vTextPaint = Paint()
        vTextPaint!!.textSize = textSize
        vTextPaint!!.color = ContextCompat.getColor(context, R.color.ink_5)
        vTextPaint!!.textAlign = Paint.Align.CENTER

        //显示单位文字
        hintPaint = Paint()
        hintPaint!!.textSize = hintSize
        hintPaint!!.color = hintColor
        hintPaint!!.textAlign = Paint.Align.CENTER

        //显示标题文字
        curSpeedPaint = Paint()
        curSpeedPaint!!.textSize = curSpeedSize
        curSpeedPaint!!.color = hintColor
        curSpeedPaint!!.textAlign = Paint.Align.CENTER
        mDrawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        sweepGradient = SweepGradient(centerX, centerY, colors, null)
        rotateMatrix = Matrix()
    }

    override fun onDraw(canvas: Canvas) {
        //抗锯齿
        canvas.drawFilter = mDrawFilter
        if (isNeedDial) {
            //画刻度线
            for (i in 0..39) {
                if (i in 16..24) {
                    canvas.rotate(9f, centerX, centerY)
                    continue
                }
                if (i % 5 == 0) {
                    degreePaint!!.strokeWidth = dipToPx(2f).toFloat()
                    degreePaint!!.color = Color.parseColor(longDegreeColor)
                    canvas.drawLine(
                        centerX,
                        centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE,
                        centerX,
                        centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - longdegree,
                        degreePaint!!
                    )
                } else {
                    degreePaint!!.strokeWidth = dipToPx(1.4f).toFloat()
                    degreePaint!!.color = Color.parseColor(shortDegreeColor)
                    canvas.drawLine(
                        centerX,
                        centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2,
                        centerX,
                        centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 - shortdegree,
                        degreePaint!!
                    )
                }
                canvas.rotate(9f, centerX, centerY)
            }
        }

        //整个弧
        canvas.drawArc(bgRect!!, startAngle, sweepAngle, false, allArcPaint!!)

        //设置渐变色
        rotateMatrix!!.setRotate(125f, centerX, centerY)
        sweepGradient!!.setLocalMatrix(rotateMatrix)
        progressPaint!!.shader = sweepGradient

        //当前进度
        canvas.drawArc(bgRect!!, startAngle, currentAngle, false, progressPaint!!)
        if (isNeedContent) {
            canvas.drawText(
                String.format("%.0f", dataBMI),
                centerX,
                centerY + textSize / 3,
                vTextPaint!!
            )
        }
        if (isNeedUnit) {
            hintPaint!!.color = hintColor
            canvas.drawText(hintString!!, centerX, centerY + 2 * textSize / 3, hintPaint!!)
        }
        if (isNeedTitle) {
            canvas.drawText(titleString!!, centerX, centerY - 2 * textSize / 3, curSpeedPaint!!)
        }
        invalidate()
    }

    /**
     * 设置最大值
     *
     * @param maxValues
     */
    fun setMaxValues(maxValues: Float) {
        this.maxValues = maxValues
        k = sweepAngle / maxValues
    }

    /**
     * 设置当前值
     *
     * @param currentValues
     */
    fun setCurrentValues(bmi: Float, isChild: Boolean) {
        val totalValue = if (isChild) { 100f } else { 40f }
        dataBMI = bmi
        var currentValues = bmi / totalValue * 100
        if (currentValues > maxValues) {
            currentValues = maxValues
        }
        if (currentValues < 0) {
            currentValues = 0f
        }

        hintColor = if(isChild){
            listColorChild[DeviceUtil.levelBMChild(bmi.toDouble())-1]
        }else{
            listColorAdult[DeviceUtil.levelBMIAdult(bmi.toDouble())-1]
        }

        hintString = if(isChild){
            listStringChild[DeviceUtil.levelBMChild(bmi.toDouble())-1]
        }else{
            listStringAdult[DeviceUtil.levelBMIAdult(bmi.toDouble())-1]
        }

        curValues = currentValues
        lastAngle = currentAngle
        setAnimation(lastAngle, currentValues * k, aniSpeed)
    }

    /**
     * 设置整个圆弧宽度
     *
     * @param bgArcWidth
     */
    fun setBgArcWidth(bgArcWidth: Int) {
        this.bgArcWidth = bgArcWidth.toFloat()
    }

    /**
     * 设置进度宽度
     *
     * @param progressWidth
     */
    fun setProgressWidth(progressWidth: Int) {
        this.progressWidth = progressWidth.toFloat()
    }

    /**
     * 设置速度文字大小
     *
     * @param textSize
     */
    fun setTextSize(textSize: Int) {
        this.textSize = textSize.toFloat()
    }

    /**
     * 设置单位文字大小
     *
     * @param hintSize
     */
    fun setHintSize(hintSize: Int) {
        this.hintSize = hintSize.toFloat()
    }

    /**
     * 设置单位文字
     *
     * @param hintString
     */
    fun setUnit(hintString: String?) {
        this.hintString = hintString
        invalidate()
    }

    /**
     * 设置直径大小
     *
     * @param diameter
     */
    fun setDiameter(diameter: Int) {
        this.diameter = dipToPx(diameter.toFloat())
    }

    /**
     * 设置标题
     *
     * @param title
     */
    private fun setTitle(title: String) {
        titleString = title
    }

    /**
     * 设置是否显示标题
     *
     * @param isNeedTitle
     */
    private fun setIsNeedTitle(isNeedTitle: Boolean) {
        this.isNeedTitle = isNeedTitle
    }

    /**
     * 设置是否显示单位文字
     *
     * @param isNeedUnit
     */
    private fun setIsNeedUnit(isNeedUnit: Boolean) {
        this.isNeedUnit = isNeedUnit
    }

    /**
     * 设置是否显示外部刻度盘
     *
     * @param isNeedDial
     */
    private fun setIsNeedDial(isNeedDial: Boolean) {
        this.isNeedDial = isNeedDial
    }

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private fun setAnimation(last: Float, current: Float, length: Int) {
        progressAnimator = ValueAnimator.ofFloat(last, current)
        progressAnimator?.duration = (length.toLong())
        progressAnimator?.setTarget(currentAngle)
        progressAnimator?.addUpdateListener { animation ->
            currentAngle = animation.animatedValue as Float
            curValues = currentAngle / k
        }
        progressAnimator?.start()
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private fun dipToPx(dip: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dip * density + 0.5f * if (dip >= 0) 1 else -1).toInt()
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private val screenWidth: Int
        get() {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }
}