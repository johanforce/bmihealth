package com.jarvis.bmihealth.presentation.heartrate.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.extensions.dpToPx
import com.jarvis.bmihealth.common.roundCelling
import com.jarvis.bmihealth.common.enums.MeasurementGraphTypeEnum

@Suppress("unused")
@SuppressLint("ViewConstructor")
class LineChartMarkerView(
    context: Context?,
    layoutResource: Int,
    private val graphChart: LineChart,
    private val typeS: Int,
    private val resolution: Int = 0,
    private val unit: String = "",
    private val typeTime: Int
) : MarkerView(context, layoutResource) {

    private val layoutMarker = findViewById<View>(R.id.bg)
    private val bg = findViewById<View>(R.id.layoutMarker)
    private val bgLeft = findViewById<View>(R.id.layoutMarkerLeft)
    private val bgRight = findViewById<View>(R.id.layoutMarkerRight)
    private val tvSys = findViewById<TextView>(R.id.tvSys)
    private val tvDias = findViewById<TextView>(R.id.tvDias)

    private val tvSysLeft = findViewById<TextView>(R.id.tvSysLeft)
    private val tvSysRight = findViewById<TextView>(R.id.tvDias)
    private val tvDiasLeft = findViewById<TextView>(R.id.tvDiasLeft)
    private val tvDiasRight = findViewById<TextView>(R.id.tvDiasRight)

    private val strokePaint = Paint()
    private val backgroundPaint = Paint()

    private var isNeedShowMarker = false

    companion object {
        const val ARROW_SIZE = 40
        private const val CIRCLE_OFFSET = 10f
        private const val STROKE_WIDTH = 3f
        private const val PADDING_START = 35
    }

    init {
        strokePaint.apply {
            strokeWidth = STROKE_WIDTH
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            context?.let { color = ContextCompat.getColor(it, R.color.transparent) }
        }

        backgroundPaint.apply {
            style = Paint.Style.FILL
            context?.let { color = ContextCompat.getColor(it, R.color.transparent) }
        }
    }

    // this function is automation call if user click in graph data
    // setup value for systolic & diastolic
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e ?: kotlin.run {
            super.refreshContent(e, highlight)
            return
        }
        if (e.x == 0F && e.y == 0F) {
            isNeedShowMarker = false
            layoutMarker.visibility = View.GONE
            return
        } else {
            isNeedShowMarker = true
            layoutMarker.visibility = View.VISIBLE

            val value = context.getString(
                R.string.analysis_vs_last_week,
                e.y.roundCelling(resolution),
                unit
            )
            val text = Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT)
            tvSys.setText(text, TextView.BufferType.SPANNABLE)
            tvSysLeft.setText(text, TextView.BufferType.SPANNABLE)
            tvSysRight.setText(text, TextView.BufferType.SPANNABLE)
            tvDiasLeft.visibility = View.GONE
            tvDiasRight.visibility = View.GONE
            tvDias.visibility = View.GONE

            showTooltips(e)
        }
        super.refreshContent(e, highlight)
    }

    private fun showTooltips(e: Entry) {
        if (e.y == 0f) return
        when (typeTime) {
            MeasurementGraphTypeEnum.Week.index -> {
                if (e.x == 7f || e.x == 6f) {
                    showTooltipsRight()
                } else {
                    showTooltipsMid()
                }
            }
            MeasurementGraphTypeEnum.Month.index -> {
                if (e.x == 4f) {
                    showTooltipsRight()
                } else {
                    showTooltipsMid()
                }
            }
            MeasurementGraphTypeEnum.Year.index -> {
                if (e.x == 11f || e.x == 12f) {
                    showTooltipsRight()
                } else {
                    showTooltipsMid()
                }
            }
        }
    }

    private fun showTooltipsRight() {
        bgRight.visibility = View.VISIBLE
        bg.visibility = View.GONE
        bgLeft.visibility = View.GONE
    }

    private fun showTooltipsMid() {
        bgRight.visibility = View.GONE
        bg.visibility = View.VISIBLE
        bgLeft.visibility = View.GONE
    }

    /**
     * override getOffsetForDrawingAtPoint function
     * used to calculate offset to draw marker
     * @param posX is x position
     * @param posY is y position
     */
    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val width = width.toFloat()
        val height = height.toFloat()
        if (posY <= height + ARROW_SIZE) {
            // If the y coordinate of the point is less than the height of the markerView,
            // if it is not processed, it will exceed the upper boundary.
            // After processing, the arrow is up at this time,
            // and we need to move the icon down by the size of the arrow
            offset.y = ARROW_SIZE.toFloat()
        } else {
            // Otherwise, it is normal, because our default is that the arrow is facing downwards,
            // and then the normal offset is that you need to offset the height of the markerView and the arrow size,
            // plus a stroke width, because you need to see the upper border of the dialog box
            offset.y = -height
        }
        // If it exceeds the right boundary, align markerView to the left
        if (posX > graphChart.width - width + PADDING_START.dpToPx()) {
            offset.x = -width
        } else {
            // By default, no offset (because the point is in the upper left corner)
            offset.x = 0f
            if (posX > width / 2) {
                // If it is greater than half of the markerView, the arrow is in the middle,
                // so it is offset by half the width to the right
                offset.x = -(width / 2)
            }
        }
        return offset
    }

    /**
     * override onDraw function to custom marker
     * @param canvas is canvas object
     * @param posX is x position
     * @param posY is y position
     */
    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        if (isNeedShowMarker.not()) return
        val offset = getOffsetForDrawingAtPoint(posX, posY)
        val saveId: Int = canvas.save()
        val width = width.toFloat()
        val height = height.toFloat()

        val path = Path()
        if (posY < height + ARROW_SIZE) {
            // Processing exceeds the upper boundary
            initPathForUpperCase(path, posX, width, height, offset, posY)
        } else {
            // Does not exceed the upper boundary
            initPathForLowerCase(path, width, height, posX, offset, posY)
        }

        // translate to the correct position and draw
        drawChartMarker(canvas, path, posX, offset, posY, saveId)
    }

    /**
     * override onDraw function to custom marker
     * @param canvas is canvas object
     * @param path is Path to draw
     * @param posX is x position
     * @param posY is y position
     * @param offset is offset or marker
     * @param saveId is saveId of canvas
     */
    private fun drawChartMarker(
        canvas: Canvas,
        path: Path,
        posX: Float,
        offset: MPPointF,
        posY: Float,
        saveId: Int
    ) {
        canvas.drawPath(path, backgroundPaint)
        canvas.drawPath(path, strokePaint)
        canvas.translate(posX + offset.x, posY + offset.y)
        draw(canvas)
        canvas.restoreToCount(saveId)
    }

    /**
     * override onDraw function to custom marker
     * @param path is Path to draw
     * @param posX is x position
     * @param posY is y position
     * @param offset is offset or marker
     * @param width is width of chart
     * @param height is height of chart
     */
    private fun initPathForLowerCase(
        path: Path,
        width: Float,
        height: Float,
        posX: Float,
        offset: MPPointF,
        posY: Float
    ) {
        path.moveTo(0F, 0F)
        path.lineTo(0 + width, 0F)
        path.lineTo(0 + width, 0 + height)
        if (posX > graphChart.width - width + PADDING_START.dpToPx()) {
            path.lineTo(width, height + ARROW_SIZE - CIRCLE_OFFSET)
            path.lineTo(width - ARROW_SIZE, 0 + height)
            path.lineTo(0F, 0 + height)
        } else {
            if (posX > width / 2) {
                path.lineTo(width / 2 + ARROW_SIZE / 2, 0 + height)
                path.lineTo(width / 2, height + ARROW_SIZE - CIRCLE_OFFSET)
                path.lineTo(width / 2 - ARROW_SIZE / 2, 0 + height)
                path.lineTo(0F, 0 + height)
            } else {
                path.lineTo(0F + ARROW_SIZE, 0 + height)
                path.lineTo(0F, height + ARROW_SIZE - CIRCLE_OFFSET)
                path.lineTo(0F, 0 + height)
            }
        }
        path.lineTo(0F, 0F)
        path.offset(posX + offset.x, posY + offset.y)
    }

    /**
     * override onDraw function to custom marker
     * @param path is Path to draw
     * @param posX is x position
     * @param posY is y position
     * @param offset is offset or marker
     * @param width is width of chart
     * @param height is height of chart
     */
    private fun initPathForUpperCase(
        path: Path,
        posX: Float,
        width: Float,
        height: Float,
        offset: MPPointF,
        posY: Float
    ) {
        path.moveTo(0F, 0F)
        if (posX > graphChart.width - width) {
            // Exceed the right boundary
            path.lineTo(width - ARROW_SIZE, 0F)
            path.lineTo(width, -ARROW_SIZE + CIRCLE_OFFSET)
            path.lineTo(width, 0F)
        } else {
            if (posX > width / 2) {
                // In the middle of the chart
                path.lineTo(width / 2 - ARROW_SIZE / 2, 0F)
                path.lineTo(width / 2, -ARROW_SIZE + CIRCLE_OFFSET)
                path.lineTo(width / 2 + ARROW_SIZE / 2, 0F)
            } else {
                // Exceed the left margin
                path.lineTo(0F, -ARROW_SIZE + CIRCLE_OFFSET)
                path.lineTo(0F + ARROW_SIZE, 0F)
            }
        }
        path.lineTo(0 + width, 0F)
        path.lineTo(0 + width, 0 + height)
        path.lineTo(0F, 0 + height)
        path.lineTo(0F, 0F)
        path.offset(posX + offset.x, posY + offset.y)
    }
}
