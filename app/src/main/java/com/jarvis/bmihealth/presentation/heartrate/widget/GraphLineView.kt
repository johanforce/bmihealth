package com.jarvis.bmihealth.presentation.heartrate.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.presentation.common.Constant
import com.jarvis.bmihealth.common.roundCellingGraph
import com.jarvis.bmihealth.common.roundFloorGraph
import com.jarvis.bmihealth.common.enums.MeasurementGraphTypeEnum
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

class GraphLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val viewChart by lazy {
        inflate(context, R.layout.view_graph_linechart, this)
    }
    private val title by lazy {
        viewChart.findViewById<TextView>(R.id.titleChart)
    }
    private val graph by lazy {
        viewChart.findViewById<LineChart>(R.id.graphChart)
    }

    var unit: String = context.getString(R.string.unit_km)

    private var resolution = 0

    private var onClickListener: OnClickListener? = null

    init {
        setupGraph()
    }

    /**
     * interface set on click in graph
     */
    override fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
    }

    /**
     * func draw graph
     * @param typeTime: type time input
     * @param typeVital: type vital input
     * @param timeStart: time start input
     * @param timeEnd: time end input
     * @param lineEntries: list Entry
     */
    fun drawLineChart(
        typeTime: Int,
        typeVital: Int,
        lineEntries: ArrayList<Entry>,
        timeStart: Date,
        timeEnd: Date,
        resolution: Int = 0,
    ) {
        val list = setupDataListEntries(lineEntries)
        val lineDataSet = setupLineDateSet(list)
        setupColorLine(list, lineDataSet)

        graph?.data = LineData(lineDataSet)

        val xAxis = graph?.xAxis
        //  setupGraph()
        setupDataXAxis(xAxis, typeTime, timeStart, timeEnd)
        setupDataYAxis(typeVital, list.map { it.y })

        graph?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    handleValueSelectedChart(typeTime, typeVital)
                }
            }

            override fun onNothingSelected() {
                // Do Nothing
            }
        })

        this.resolution = resolution
        graph?.invalidate()
    }

    private fun setupGraph() {
        graph?.run {
            setTouchEnabled(true)
            setPinchZoom(false)
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false

            isHighlightPerTapEnabled = true
            isClickable = true

            setDrawBorders(false)
            setDrawGridBackground(false)

            description?.isEnabled = false
            legend?.isEnabled = false

            setXAxisRenderer(
                CustomXAxisRenderer(
                    graph?.viewPortHandler,
                    graph?.xAxis,
                    graph?.getTransformer(YAxis.AxisDependency.LEFT)
                )
            )
            // set position left chart in screen
            extraLeftOffset = 5f
            // set position right chart in screen
            extraRightOffset = 5f
            // set position bottom chart in screen
            extraBottomOffset = 20f
        }

        graph.extraTopOffset = 70.0f

        graph?.axisLeft?.run {
            setDrawGridLines(true)
            setDrawLabels(true)
            setDrawAxisLine(false)
            setDrawLimitLinesBehindData(true)
            textColor = context.getColor(R.color.ink_5)
            textSize = 10f
        }

        graph?.xAxis?.run {
            setDrawGridLines(false)
            setDrawLabels(true)
            setDrawAxisLine(false)
            textSize = 9f
            labelCount = 13
            spaceMax = 0.5f
            spaceMin = 0.5f
            yOffset = 20f
            position = XAxis.XAxisPosition.BOTTOM
        }

        graph?.axisRight?.run {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
    }

    /**
     * Handle value in graph
     * @param typeTime: type time input
     * @param typeVital: type vital input
     */
    private fun handleValueSelectedChart(typeTime: Int, typeVital: Int) {
        val graphChart = graph ?: return
        graphChart.marker = LineChartMarkerView(
            context,
            R.layout.layout_graph_chat_marker,
            graphChart,
            typeVital,
            resolution,
            unit,
            typeTime
        )
    }

    /**
     * find label in graph
     * @param typeVital: type vital input
     * @param list: list Entry
     */
    private fun setupDataYAxis(typeVital: Int, list: List<Float>) {
        val minValue = list.filterNot { it == 0f }.minOrNull() ?: 0f
        val maxValue = list.filterNot { it == 0f }.maxOrNull() ?: 0f
        when (typeVital) {
            Constant.TYPE_WEIGHT -> setupLeftAxisForWeight(minValue, maxValue)
            Constant.TYPE_PULSE -> setupLeftAxisForPulse(minValue, maxValue)
        }
    }

    /**
     * Configure yAxis for weight chart
     * @param minValue: [Float] - min value of entries
     * @param maxValue: [Float] - max value of entries
     */
    private fun setupLeftAxisForWeight(minValue: Float, maxValue: Float) {
        val isEmptyEntries = minValue == 0f && minValue == maxValue
        val (floor, celling, step) = when {
            (minValue >= 0 && minValue < 90f && maxValue >= 0 && maxValue < 90f) || isEmptyEntries -> {
                Triple(0f, 90f, 10)
            }
            else -> Triple(
                first = max(0f, minValue.roundFloorGraph()),
                second = maxValue.roundCellingGraph(),
                third = if ((maxValue - minValue) <= 10) 5 else 10
            )
        }
        graph?.axisLeft?.apply {
            axisMinimum = floor
            axisMaximum = celling
            setLabelCount(calculateLabelCount(celling, floor, step), true)
        }
    }

    /**
     * Configure yAxis for pulse chart
     * @param minValue: [Float] - min value of entries
     * @param maxValue: [Float] - max value of entries
     */
    private fun setupLeftAxisForPulse(minValue: Float, maxValue: Float) {
        val isEmptyEntries = minValue == 0f && minValue == maxValue
        val (floor, celling, step) = when {
            (minValue > 60f && minValue < 100f && maxValue > 60f && maxValue < 100f) || isEmptyEntries -> {
                Triple(60f, 100f, 5)
            }
            else -> Triple(
                first = max(0f, minValue.roundFloorGraph()),
                second = maxValue.roundCellingGraph(),
                third = if ((maxValue - minValue) <= 40) 5 else 10
            )
        }
        graph?.axisLeft?.apply {
            axisMinimum = floor
            axisMaximum = celling
            setLabelCount(calculateLabelCount(celling, floor, step), true)
        }
    }

    /**
     * Graph only draw number of labels in range [2 to 25]
     */
    private fun calculateLabelCount(celling: Float, floor: Float, step: Int): Int {
        var labelCount = ((celling - floor) / step).toInt() + 1
        var stepValue = step
        while (labelCount > 25) {
            stepValue += 5
            labelCount = ((celling - floor) / stepValue).toInt() + 1
        }
        while (labelCount < 2 && stepValue > 0) {
            stepValue--
            labelCount = ((celling - floor) / stepValue).toInt() + 1
        }
        return labelCount
    }

    /**
     * find label in graph
     * @param xAxis: horizontal axis Point
     * @param typeTime: type time input
     * @param timeStart: time start input
     * @param timeEnd: time end input
     */
    private fun setupDataXAxis(xAxis: XAxis?, typeTime: Int, timeStart: Date, timeEnd: Date) {
        // item bottom
        xAxis?.let {
            it.granularity = 1f
        }
        when (typeTime) {
            MeasurementGraphTypeEnum.Week.index -> {
                xAxis?.let {
                    it.axisMaximum = 7f
                    it.valueFormatter = object : IndexAxisValueFormatter(
                        getNameLabelWeek(
                            timeStart,
                            timeEnd
                        )
                    ) {
                    }
                }
            }
            MeasurementGraphTypeEnum.Month.index -> {
                xAxis?.let {
                    it.axisMaximum = 4f
                    it.valueFormatter = object : IndexAxisValueFormatter(
                        getNameLabelMonth(
                            timeEnd
                        )
                    ) {
                    }
                }
            }
            MeasurementGraphTypeEnum.Year.index -> {
                xAxis?.let {
                    it.axisMaximum = 12f
                    it.valueFormatter = object : IndexAxisValueFormatter(
                        getNameLabelYear(
                            timeEnd
                        ).reversed()
                    ) {
                    }
                }
            }
        }
    }

    /**
     * set color line point in Graph
     * @param list: list Entry
     * @param lineDataSet: list LineDataSet
     */
    private fun setupColorLine(list: ArrayList<Entry>, lineDataSet: LineDataSet) {
        // Line
        val colorArray = arrayListOf<Int>()
        var i = 0
        while (i < list.size - 1) {
            if (list[i].y == 0f || list[i + 1].y == 0f) {
                colorArray.add(ContextCompat.getColor(context, R.color.transparent))
            } else {
                colorArray.add(ContextCompat.getColor(context, R.color.color_steps))
            }
            i++
        }
        if (colorArray.isEmpty()) {
            colorArray.add(ContextCompat.getColor(context, R.color.transparent))
        }

        // Point
        val colorPointArray = arrayListOf<Int>()
        var j = 0
        while (j < list.size) {
            if (list[j].y == 0f) {
                colorPointArray.add(ContextCompat.getColor(context, R.color.transparent))
            } else {
                colorPointArray.add(ContextCompat.getColor(context, R.color.color_steps))
            }
            j++
        }
        if (colorPointArray.isEmpty()) {
            colorPointArray.add(ContextCompat.getColor(context, R.color.transparent))
        }
        lineDataSet.colors = colorArray
        lineDataSet.lineWidth = 2f

        if (minMaxListEntry(list).first == 0f && minMaxListEntry(list).second == 0f) {
            lineDataSet.setCircleColor(ContextCompat.getColor(context, R.color.transparent))
        } else {
            lineDataSet.circleColors = colorPointArray
        }
        lineDataSet.mode = LineDataSet.Mode.LINEAR

        // Point
        lineDataSet.circleRadius = 6f
        lineDataSet.setDrawCircles(true)
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.valueTextSize = 12f
        lineDataSet.valueTextColor = ContextCompat.getColor(context, R.color.transparent)
    }

    /**
     * find Entry in graph
     * @param list: list Entry
     */
    private fun setupLineDateSet(list: ArrayList<Entry>): LineDataSet {
        val lineDataSet = LineDataSet(list, "Work")
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT

        // Target Zone
        lineDataSet.isHighlightEnabled = true
        lineDataSet.setDrawHighlightIndicators(true)
        lineDataSet.setDrawVerticalHighlightIndicator(true)
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        lineDataSet.highlightLineWidth = 24f
        lineDataSet.highLightColor =
            ContextCompat.getColor(context, R.color.pri_5)
        return lineDataSet
    }

    /**
     *function data smoothing
     * @param lineEntries: list entry before smoothing
     */
    private fun setupDataListEntries(lineEntries: ArrayList<Entry>): ArrayList<Entry> {
        var temp = 0
        val list = arrayListOf<Entry>()
        for (item in lineEntries) {
            if (item.y != 0f) {
                list.add(item)
                temp++
            }
            if (temp == 0 && item.y == 0f) {
                list.add(item)
            }
        }
        return list
    }

    @SuppressLint("SimpleDateFormat")
    /**
     * get list label week
     * @param timeEnd: time end in selected time
     * @param timeStart: time start in selected time
     */
    private fun getNameLabelWeek(timeStart: Date, timeEnd: Date): ArrayList<String> {
        val xAxisLabel: ArrayList<String> = ArrayList()
        val calendar = Calendar.getInstance()
        calendar.time = timeStart

        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = timeEnd

        var temp = 0
        while (temp < 8) {
            if (temp == 0 || calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                xAxisLabel.add(
                    SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.time)
                        .toString() + "\n" + calendar.get(
                        Calendar.DAY_OF_MONTH
                    ).toString()
                )
            } else {
                xAxisLabel.add("\n" + calendar.get(Calendar.DAY_OF_MONTH).toString())
            }
            temp++
            calendar.add(Calendar.DATE, 1)
        }

        return xAxisLabel
    }

    /**
     * get list label month
     * @param timeEnd: time end in selected time
     */
    private fun getNameLabelMonth(timeEnd: Date): List<String> {
        val xAxisLabel: ArrayList<String> = ArrayList()
        val calendar = Calendar.getInstance()
        calendar.time = timeEnd

        var i = 0

        while (i < 5) {
            val calendarStart = Calendar.getInstance()
            calendarStart.time = calendar.time
            calendarStart.add(Calendar.DATE, -6)

            xAxisLabel.add(textMonth(calendarStart.time) + "\n-" + textMonth(calendar.time))

            calendar.add(Calendar.DATE, -7)
            i++
        }

        return xAxisLabel.reversed()
    }

    /**
     * get list label year
     * @param timeEnd: time end in selected time
     */
    private fun getNameLabelYear(timeEnd: Date): ArrayList<String> {
        val xAxisLabel: ArrayList<String> = ArrayList()
        val calendar = Calendar.getInstance()
        calendar.time = timeEnd

        var i = 0

        while (i < 13) {
            if (i == 0 || i == 12) {

                xAxisLabel.add(textYear(calendar.time))
            } else {
                xAxisLabel.add(textDateYear(calendar.time))
            }

            calendar.add(Calendar.MONTH, -1)
            i++
        }
        return xAxisLabel
    }

    @SuppressLint("SimpleDateFormat")
    /**
     * convert time Date to month string
     * @param time: Date
     */
    private fun textMonth(time: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = time
        return SimpleDateFormat("dd MMM", Locale.ENGLISH).format(calendar.time)
    }

    /**
     * convert time Date to year string
     * @param time: Date
     */
    private fun textDateYear(time: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = time

        val month = calendar.get(Calendar.MONTH) + 1
        return "\n" + month
    }

    /**
     * convert time Date to String if start year
     * @param time: Date
     */
    private fun textYear(time: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = time

        val month = calendar.get(Calendar.MONTH) + 1
        return "" + calendar.get(Calendar.YEAR) + "\n" + month
    }

    /**
     * set title in graph
     */
    fun setTitle(titleText: String) {
        title?.text = titleText
    }

    /**
     * create list label in graph
     */
    class CustomXAxisRenderer(
        viewPortHandler: ViewPortHandler?,
        xAxis: XAxis?,
        trans: Transformer?
    ) :
        XAxisRenderer(viewPortHandler, xAxis, trans) {
        override fun drawLabel(
            c: Canvas,
            formattedLabel: String,
            x: Float,
            y: Float,
            anchor: MPPointF,
            angleDegrees: Float
        ) {
            val line = formattedLabel.split("\n").toTypedArray()
            Utils.drawXAxisValue(
                c,
                line[0], x, y, mAxisLabelPaint, anchor, angleDegrees
            )
            if (line.size > 1) {
                Utils.drawXAxisValue(
                    c,
                    line[1],
                    x,
                    y + mAxisLabelPaint.textSize,
                    mAxisLabelPaint,
                    anchor,
                    angleDegrees
                )
            }
        }
    }

    /**
     * get value Pair<min, max> in list Entry
     * @param lineEntries: list Entry
     */
    private fun minMaxListEntry(lineEntries: List<Entry>): Pair<Float, Float> {
        val minValue = lineEntries.minByOrNull { it.y }?.y ?: 0f
        val maxValue = lineEntries.maxByOrNull { it.y }?.y ?: 0f
        return minValue to maxValue
    }
}
