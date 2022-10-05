@file:Suppress("unused")

package com.jarvis.bmihealth.presentation.bmiother.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ViewItemRequiredBinding

class ViewItemRequired : FrameLayout {
    private var binding: ViewItemRequiredBinding? = null
    private var title: String? = null
    private var unit: String? = null
    private var icon: Drawable? = null
    private var index: String? = null
    private var attributeArray: TypedArray? = null

    constructor(context: Context) : super(context) {
        initView(null, context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs, context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs, context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(attrs, context)
    }

    private fun initView(attrs: AttributeSet?, context: Context) {
        val inflater =
            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewItemRequiredBinding.inflate(inflater, this, true)
        if (attrs == null) {
            return
        }
        try {
            attributeArray = context.obtainStyledAttributes(attrs, R.styleable.ViewItemRequired)
            title = attributeArray!!.getString(R.styleable.ViewItemRequired_title)
            unit = attributeArray!!.getString(R.styleable.ViewItemRequired_unit)
            index = attributeArray!!.getString(R.styleable.ViewItemRequired_index)
            icon = attributeArray!!.getDrawable(R.styleable.ViewItemRequired_icon)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding!!.tvTitle.text = title
        binding!!.tvIndex.text = index
        binding!!.tvUnit.text = unit
        if (icon != null) {
            binding!!.ivStartIcon.setImageDrawable(icon)
            binding!!.ivStartIcon.visibility = VISIBLE
        }
    }

    fun setTitle(title: String?) {
        binding!!.tvTitle.text = title
    }

    fun setIndex(index: String?) {
        binding!!.tvIndex.text = index
    }

    fun setUnit(unit: String?) {
        binding!!.tvUnit.text = unit
    }

    fun setStartIcon(id: Int) {
        binding!!.ivStartIcon.setImageDrawable(
            ContextCompat.getDrawable(
                context!!, id
            )
        )
        binding!!.ivStartIcon.visibility = VISIBLE
    }
}