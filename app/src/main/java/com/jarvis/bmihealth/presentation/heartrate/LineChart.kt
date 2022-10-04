package com.jarvis.bmihealth.presentation.heartrate

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.presentation.utilx.dpToPx
import com.jarvis.bmihealth.presentation.utilx.roundCellingGraph
import com.jarvis.bmihealth.presentation.utilx.roundFloorGraph
import kotlin.math.abs

/**
 * Chart that draws lines, surfaces, circles, ...
 */
class LineChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : BarLineChartBase<LineData?>(context, attrs, defStyle), LineDataProvider {

    private var thresholds = emptyList<Pair<Int, String?>>()

    private val thresholdTextPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.color_steps)
            textSize = axisLeft.textSize
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
        }
    }

    private val thresholdRectPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.bg_01)
            isAntiAlias = true
        }
    }

    override fun init() {
        super.init()
        mRenderer = LineChartRenderer(this, mAnimator, mViewPortHandler)
    }

    override fun getLineData(): LineData {
        return mData!!
    }

    override fun onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer is LineChartRenderer) {
            (mRenderer as LineChartRenderer).releaseBitmap()
        }
        super.onDetachedFromWindow()
    }

    /**
     * Set Thresholds
     * @param thresholds: [List] of value and title
     */
    fun setThresholds(thresholds: List<Pair<Int, String?>>) {
        this.thresholds = thresholds
        axisLeft.removeAllLimitLines()
        thresholds.forEach { (threshold, title) ->
            drawThresholdLine(threshold, title ?: "")
        }
    }

    /**
     * Setting high line
     */
    private fun drawThresholdLine(threshold: Int, title: String = "") {
        // if axisMinimum > low target -> axisMaximum = low target
        if (axisLeft.axisMinimum > threshold) {
            axisLeft.axisMinimum = threshold.roundFloorGraph().toFloat()
        }
        if (axisLeft.axisMaximum < threshold) {
            axisLeft.axisMaximum = threshold.roundCellingGraph().toFloat()
        }
        val isTop =
            (axisLeft.axisMaximum - threshold) / (axisLeft.axisMaximum - axisLeft.axisMinimum) > 0.07
        val highLimit = createHighLimit(
            threshold.toFloat(),
            title,
            isTop
        )
        axisLeft.addLimitLine(highLimit)
    }

    /**
     * Create an limit line
     */
    private fun createHighLimit(value: Float, title: String, isTop: Boolean): LimitLine {
        return LimitLine(value, title).apply {
            // set label color in Line
            this.textColor = ContextCompat.getColor(context, R.color.color_steps)
            // set color Line
            this.lineColor = ContextCompat.getColor(context, R.color.color_steps)
            // set width Line
            lineWidth = 1f
            // set position Line
            labelPosition = if (isTop) {
                LimitLine.LimitLabelPosition.RIGHT_TOP
            } else {
                LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            }
            textSize = THRESHOLD_TEXT_SIZE
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (axisLeft.limitLines.isNotEmpty()) {
            drawThreshold(canvas)
            drawMarkers(canvas)
        }
    }

    /**
     * this function used to draw threshold value
     * @param canvas is draw object
     */
    private fun drawThreshold(canvas: Canvas?) {
        val maxXValue = axisLeft.mAxisMaximum.toDouble()
        val minXValue = axisLeft.axisMinimum.toDouble()
        val chartHeight = viewPortHandler.contentBottom() - viewPortHandler.contentTop()
        val displayXValue = List(axisLeft.labelCount) {
            minXValue + (maxXValue - minXValue) * it / (axisLeft.labelCount - 1)
        }

        val thresholdTextFactor = THRESHOLD_TEXT_FACTOR.dpToPx()

        // x position of threshold label
        val xPosition = viewPortHandler.contentLeft() - thresholdTextFactor
        thresholds.forEach { (value, _) ->
            if (value.toDouble() in minXValue..maxXValue) {
                // y position of threshold label
                val yPosition =
                    chartHeight - (chartHeight * (value - minXValue) / (maxXValue - minXValue)) +
                        viewPortHandler.contentTop() + THRESHOLD_Y_FACTOR

                // draw rectangle to remove closest value
                drawRectIfNeeded(canvas, displayXValue, value, minXValue, maxXValue)
                // draw threshold text
                canvas?.drawText(
                    value.toString(),
                    xPosition,
                    yPosition.toFloat(),
                    thresholdTextPaint
                )
            }
        }
    }

    /**
     * This is function is used to draw white rectangle - for remove closest value
     * @param canvas is Canvas object
     * @param displayXValue is all x values
     * @param threshold is threshold value
     * @param minXValue is min of x value
     * @param maxXValue is max of x value
     */
    private fun drawRectIfNeeded(
        canvas: Canvas?,
        displayXValue: List<Double>,
        threshold: Int,
        minXValue: Double,
        maxXValue: Double
    ) {
        val chartHeight = viewPortHandler.contentBottom() - viewPortHandler.contentTop()
        val yPosition =
            chartHeight - (chartHeight * (threshold - minXValue) / (maxXValue - minXValue)) +
                viewPortHandler.contentTop() + THRESHOLD_Y_FACTOR
        val thresholdTextFactor = THRESHOLD_TEXT_FACTOR.dpToPx()

        // x position of threshold label
        val xPosition = viewPortHandler.contentLeft() - thresholdTextFactor
        displayXValue.forEach { xValue ->
            val percent = (abs(xValue - threshold) / (maxXValue - minXValue) * 100).toFloat()
            // if the closest value is less than 4% then
            // draw an rectangle to remove nearest BP value
            if (percent <= LIMIT_FACTOR) {
                // factor used to draw rectangle
                val rectFactor = if (percent <= 1) {
                    3F
                } else {
                    percent * 0.55F + 2F
                }
                if (xValue >= threshold) {
                    // draw rect above threshold
                    canvas?.drawRect(
                        0F,
                        yPosition.toFloat() - thresholdTextFactor * rectFactor,
                        xPosition + thresholdTextFactor / 2,
                        yPosition.toFloat(),
                        thresholdRectPaint
                    )
                } else {
                    // draw rect below threshold
                    canvas?.drawRect(
                        0F,
                        yPosition.toFloat() - thresholdTextFactor * LIMIT_FACTOR / 2,
                        xPosition + thresholdTextFactor / 2,
                        yPosition.toFloat() + thresholdTextFactor * rectFactor / 2,
                        thresholdRectPaint
                    )
                }
            }
        }
    }

    companion object {
        const val dot = "dot"
        const val fill = "fill"
        private const val THRESHOLD_Y_FACTOR = 10
        private const val THRESHOLD_TEXT_FACTOR = 5
        private const val THRESHOLD_TEXT_SIZE = 14F
        private const val LIMIT_FACTOR = 4F
    }
}
