package com.jarvis.bmihealth.presentation.heartrate.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.extensions.setTextColorRes
import com.jarvis.bmihealth.common.utils.daysOfWeek
import com.jarvis.bmihealth.databinding.LayoutCalenderDayLegendBinding
import com.jarvis.locale_helper.helper.LocaleHelper
import java.time.format.TextStyle
import java.util.*

class CalenderDayLegendView : FrameLayout {
    private var binding: LayoutCalenderDayLegendBinding? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val systemService =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = LayoutCalenderDayLegendBinding.inflate(systemService, this, true)
        genDisplayDayName()
    }

    private fun genDisplayDayName() {
        val daysOfWeek = daysOfWeek(context)
        val locale = LocaleHelper.getInstance().getLocale(context)
        binding!!.root.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, locale).uppercase(
                    Locale.ENGLISH
                )
                setTextColorRes(R.color.ink_3)
            }
        }
    }
}
