@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.jarvis.bmihealth.presentation.home.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ViewHomeBmiBinding
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.heathcarebmi.utils.Consts
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import com.well.unitlibrary.UnitConverter
import java.math.RoundingMode
import java.text.NumberFormat

class ViewHomeBMI : ConstraintLayout, View.OnClickListener {
    private val adultTypeResource = intArrayOf(
        R.string.bmi_very_under_weight,
        R.string.bmi_under_weight,
        R.string.bmi_healthy_weight,
        R.string.bmi_over_weight,
        R.string.bmi_moderately_obese,
        R.string.bmi_very_obese
    )
    private val childTypeResource = intArrayOf(
        R.string.bmi_very_under_weight,
        R.string.bmi_under_weight,
        R.string.bmi_healthy_weight,
        R.string.bmi_over_weight,
        R.string.bmi_obesity
    )
    private var adultBgColor =
        arrayOf("#6BD3FF", "#68A5F8", "#4ACB86", "#FFCB08", "#FF974B", "#FF5D5D")
    private var childBgColor = arrayOf("#6BD3FF", "#68A5F8", "#4ACB86", "#FFCB08", "#FF5D5D")
    private var binding: ViewHomeBmiBinding? = null
    private var bMI = 0.0
    private val bmrOther = 0.0
    private val maintainOther = 0.0
    private var bodyOther = 0.0
    private var onClickListener: OnClickListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    fun setOnClick(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
    }

    @SuppressLint("ClickableViewAccessibility")
    fun init(
        context: Context,
        userProfile: ProfileUserModel,
        isKmSetting: Boolean
    ) {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewHomeBmiBinding.inflate(layoutInflater, this, true)
        loadData(userProfile, isKmSetting)
        binding!!.clBody.setOnClickListener(this)
        binding!!.clHealthy.setOnClickListener(this)
        binding!!.clBMI.setOnClickListener(this)
        binding!!.viewBmi.setOnClickListener(this)
        binding!!.clBMI.setOnTouchListener { _: View?, motionEvent: MotionEvent? ->
            binding!!.viewBmi.onTouchEvent(motionEvent)
            false
        }
        binding!!.viewBmi.setOnTouchListener { _: View?, motionEvent: MotionEvent? ->
            binding!!.clBMI.onTouchEvent(motionEvent)
            false
        }
    }

    @SuppressLint("SetTextI18n")
    fun loadData(
        profileUserModel: ProfileUserModel,
        isKmSetting: Boolean
    ) {
        binding!!.viewBmi.setBarHeight(resources.getDimension(R.dimen.px10))
            .addBmiValueChangedListener { bmiValue: Double, _: String?, _: Int, idBMI: Int ->
                if (HealthIndexUtils.calculateAgeInMonth(profileUserModel.birthday) < Consts.MAX_CHILD_AGE_IN_MONTH) {
                    binding!!.tvStatus.setText(childTypeResource[idBMI])
                    setColorHealthyWeightChild(idBMI)
                } else {
                    binding!!.tvStatus.setText(adultTypeResource[idBMI])
                    setColorHealthyWeightAdult(idBMI)
                }
                setBmiText(profileUserModel.birthday, HealthIndexUtils.getBmi(profileUserModel.birthday, profileUserModel.gender, profileUserModel.weight, profileUserModel.height))
                bMI = bmiValue
            }
            .setBMIValue(profileUserModel.birthday, HealthIndexUtils.getBmi(profileUserModel.birthday, profileUserModel.gender, profileUserModel.weight, profileUserModel.height))
        bodyOther = HealthIndexUtils.getBodyFatPercentage(bMI, profileUserModel.gender, profileUserModel.birthday)
        if (HealthIndexUtils.calculateAgeInMonth(profileUserModel.birthday) < Consts.MAX_CHILD_AGE_IN_MONTH) {
            binding!!.clBody.visibility = GONE
        } else {
            binding!!.clBody.visibility = VISIBLE
        }
        binding!!.tvIndexFat.text = formatDoubleToString(bodyOther, 1) + ""
        val healthyWeight = HealthIndexUtils.getHealthyWeight(profileUserModel.birthday, profileUserModel.gender, profileUserModel.height)
        if (isKmSetting) {
            binding!!.tvIndexWeight.text = formatDoubleToString(
                healthyWeight.healthyWeightFrom,
                1
            ) + " - " + formatDoubleToString(healthyWeight.healthyWeightTo, 1)
            binding!!.tvKg.text = context.getString(R.string.unit_kg)
        } else {
            binding!!.tvIndexWeight.text = formatDoubleToString(
                UnitConverter.convertKgToLbs(healthyWeight.healthyWeightFrom),
                1
            ) + " - " + formatDoubleToString(
                UnitConverter.convertKgToLbs(healthyWeight.healthyWeightTo),
                1
            )
            binding!!.tvKg.text = context.getString(R.string.unit_lbs)
        }
        val age = HealthIndexUtils.calculateAgeInYear(profileUserModel.birthday)
        when {
            age < 10 -> {
                binding!!.titleBMIAge.text = context.getString(R.string.bmi_toolbar_child)
            }
            age in 10..19 -> {
                binding!!.titleBMIAge.text = context.getString(R.string.bmi_toolbar_teen)
            }
            else -> {
                binding!!.titleBMIAge.text = context.getString(R.string.bmi_toolbar_adult)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setBmiText(birthday: Long, bmi: Double) {
        if (HealthIndexUtils.calculateAgeInMonth(birthday) < Consts.MAX_CHILD_AGE_IN_MONTH) {
            var text = ""
            val bmiInt = bmi.toInt()
            if (bmiInt == 1) text = "st" else if (bmiInt == 2) text =
                "nd" else if (bmiInt == 3) text = "rd" else if (bmiInt > 3) text = "th"
            binding!!.tvBMI.text = "$bmiInt $text"
        } else {
            binding!!.tvBMI.text = HealthIndexUtils.roundBmiValue(bmi)
        }
    }

    fun disableClickView() {
        binding!!.clHealthy.background = null
        binding!!.clBMI.background = null
        binding!!.clBody.background = null
        binding!!.viewBmi.background = null
    }

    fun setColorHealthyWeightAdult(id: Int) {
        binding!!.tvStatus.setTextColor(Color.parseColor(adultBgColor[id]))
    }

    fun setColorHealthyWeightChild(id: Int) {
        binding!!.tvStatus.setTextColor(Color.parseColor(childBgColor[id]))
    }

    fun showTitleView(isShow: Boolean) {
        binding!!.ivGender.visibility = if (isShow) VISIBLE else GONE
        binding!!.titleBMIAge.visibility = if (isShow) VISIBLE else GONE
    }

    fun changeBackground() {
        binding!!.bgBMI.background = ContextCompat.getDrawable(
            context,
            R.drawable.all_bg_border_8px_bg_1
        )
        binding!!.clBMI.background = ContextCompat.getDrawable(
            context,
            R.drawable.ripple_home_bmi_ver2
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.clBody -> {
                if (onClickListener != null) {
                    onClickListener!!.onClickBody()
                }
            }
            R.id.clHealthy -> {
                if (onClickListener != null) {
                    onClickListener!!.onClickHealthy()
                }
            }
            R.id.clBMI, R.id.viewBmi -> {
                if (onClickListener != null) {
                    onClickListener!!.onClickBMI()
                }
            }
        }
    }

    interface OnClickListener {
        fun onClickHealthy()
        fun onClickBody()
        fun onClickBMI()
    }

    companion object {
        fun formatDoubleToString(value: Double?, digit: Int): String {
            val numberFormat = NumberFormat.getInstance()
            numberFormat.maximumFractionDigits = digit
            numberFormat.roundingMode = RoundingMode.HALF_UP
            return numberFormat.format(value)
        }

        fun formatDoubleToStringOne(value: Double, digit: Int): String {
            val s = "" + value
            var bmi = ""
            var temp = 0
            var checkDot = false
            if (digit != 0) {
                for (i in s.indices) {
                    bmi += s[i]
                    if (checkDot) {
                        temp++
                    }
                    if (s[i] == '.') {
                        checkDot = true
                    }
                    if (temp == digit) {
                        break
                    }
                }
            } else {
                for (i in s.indices) {
                    bmi = if (s[i] != '.') {
                        bmi + s[i]
                    } else {
                        break
                    }
                }
            }
            return bmi
        }
    }
}
