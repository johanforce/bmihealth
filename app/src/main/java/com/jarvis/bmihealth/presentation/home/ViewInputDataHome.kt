@file:Suppress("MemberVisibilityCanBePrivate")

package com.jarvis.bmihealth.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ItemTabHomeBinding
import com.jarvis.bmihealth.presentation.utilx.DeviceUtil

@Suppress("unused")
class ViewInputDataHome : ConstraintLayout {
    var binding: ItemTabHomeBinding? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context,attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }


    var onProfileListener: OnClickProfileListener? = null


    fun setOnClickListener(onClickProfile: OnClickProfileListener) {
        onProfileListener = onClickProfile
    }

    interface OnClickProfileListener {
        fun clickView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context) {
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemTabHomeBinding.inflate(inflate, this, true)
        initClickListener()
    }

    @SuppressLint("CustomViewStyleable", "ObsoleteSdkInt", "Recycle")
    private fun init(context: Context, attrs: AttributeSet?) {
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemTabHomeBinding.inflate(inflate, this, true)

        try {
            val attributeArray: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ItemTabView)
            val textDes = attributeArray.getString(R.styleable.ItemTabView_text_des)
            val textData = attributeArray.getString(R.styleable.ItemTabView_text_data)
            var drawableStart: Drawable? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawableStart =
                    attributeArray.getDrawable(R.styleable.ItemTabView_image)
            } else {
                val drawableStartId = attributeArray.getResourceId(
                    R.styleable.ItemTabView_image,
                    -1
                )
                if (drawableStartId != -1) drawableStart =
                    AppCompatResources.getDrawable(context, drawableStartId)
            }

            binding?.tvData?.text = textData
            binding?.tvDex?.text = textDes
            binding?.ivImage?.setImageDrawable(drawableStart)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        initClickListener()
    }

    fun setDataView(data: Double, isInt: Boolean){
        if(isInt){
            binding?.tvData?.text = data.toInt().toString()
        }else{
            binding?.tvData?.text = DeviceUtil.roundOffDecimal(data)
        }
    }

    private fun initClickListener() {
        binding?.clItem?.setOnClickListener {
            onProfileListener?.clickView()
        }
    }
}