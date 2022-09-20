@file:Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate", "UNUSED_VALUE")

package com.jarvis.bmihealth.presentation.register

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ViewRpeBinding
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import java.util.*

class ViewRPE : ConstraintLayout {
    private var binding: ViewRpeBinding? = null
    var currentRPE = 3
    private var isKmSetting = false
    private var stringTitle: String? = null
    private var stringDes: String? = null
    private var listenerClick: ListenerClick? = null
    var goalTemp = -1
    var activityTemp = -1

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

    fun onListener(listenerClick: ListenerClick?) {
        this.listenerClick = listenerClick
    }

    fun init(context: Context, isSetting: Boolean, activity: Int, goal: Int) {
        goalTemp = goal
        isKmSetting = isSetting
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewRpeBinding.inflate(layoutInflater, this, true)
        currentRPE = activity
        setActivityLevel(activity)
        updateRPE(activity)
        binding!!.sbEffortNew.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                if (seekParams.thumbPosition < 1) {
                    binding!!.sbEffortNew.setProgress(1f)
                    stringTitle = getContext().getString(R.string.rpe_sedentary)
                    stringDes = getContext().getString(R.string.rpe_sedentary_des)
                    setValueRPE(stringTitle, stringDes)
                }
                if (seekParams.thumbPosition < 1) {
                    activityTemp = 1
                    updateRPE(1)
                } else {
                    activityTemp = seekParams.thumbPosition
                    updateRPE(seekParams.thumbPosition)
                }
                if (listenerClick != null) {
                    listenerClick!!.dataActivityLevel(activityTemp)
                }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        }
        setTextGoal(goalTemp, isKmSetting)
        binding!!.viewGoal.setOnListenerClick {

//            listenerClick.onDialogClick();
            val dialogSetGoal = DialogSetGoal(context, goalTemp, isKmSetting)
            dialogSetGoal.show()
            dialogSetGoal.onListener(object : DialogSetGoal.ListenerClick {
                override fun closeDialog(isClickBackgroud: Boolean) {}
                override fun saveGoal(goal: Int) {
                    if (listenerClick != null) {
                        goalTemp = goal
                        listenerClick!!.dataGoal(goal)
                    }
                    setTextGoal(goal, isKmSetting)
                }
            })
        }
    }

    private fun progessToLevel(progess: Int): Int {
        return if (progess < 2) {
            1
        } else if (progess < 4) {
            1
        } else if (progess < 6) {
            2
        } else if (progess < 8) {
            3
        } else if (progess < 10) {
            4
        } else {
            5
        }
    }

    fun updateRPE(rpe: Int) {
        when (rpe) {
            1 -> {
                stringTitle = context.getString(R.string.rpe_sedentary)
                stringDes = context.getString(R.string.rpe_sedentary_des)
                setValueRPE(stringTitle, stringDes)
            }
            2 -> {
                stringTitle = context.getString(R.string.rpe_lightly)
                stringDes = context.getString(R.string.rpe_lightly_des)
                setValueRPE(stringTitle, stringDes)
            }
            3 -> {
                stringTitle = context.getString(R.string.rpe_moderately)
                stringDes = context.getString(R.string.rpe_moderately_des)
                setValueRPE(stringTitle, stringDes)
            }
            4 -> {
                stringTitle = context.getString(R.string.rpe_very_active)
                stringDes = context.getString(R.string.rpe_very_active_des)
                setValueRPE(stringTitle, stringDes)
            }
            5 -> {
                stringTitle = context.getString(R.string.rpe_extremely)
                stringDes = context.getString(R.string.rpe_extremely_des)
                setValueRPE(stringTitle, stringDes)
            }
        }
        currentRPE = rpe
    }

    fun setValueRPE(title: String?, des: String?) {
        binding!!.tvDesStatus.text = des
        binding!!.tvStatus.text = title
    }

    fun setUnit(unit: Boolean) {
        isKmSetting = unit
    }

    fun setShowDialogUnit(index: Int, isSetting: Boolean) {
        binding!!.viewGoal.setOnClickListener {
            val dialogSetGoal = DialogSetGoal(context, index, isKmSetting)
            dialogSetGoal.show()
            dialogSetGoal.onListener(object : DialogSetGoal.ListenerClick {
                override fun closeDialog(isClickBackgroud: Boolean) {}
                override fun saveGoal(goal: Int) {
                    if (listenerClick != null) {
                        listenerClick!!.dataGoal(goal)
                    }
                }
            })
        }
    }

    interface ListenerClick {
        fun dataActivityLevel(level: Int)
        fun dataGoal(goal: Int)
        fun onDialogClick()
    }

    fun setActivityLevel(level: Int) {
        binding!!.sbEffortNew.setProgress((level * 2).toFloat())
    }

    fun setTextGoal(temp: Int, isSetting: Boolean) {
        val valueWeight: String
        val des: String
        val unit: String = if (isSetting) context!!.getString(R.string.unit_kg) else {
            context!!.getString(R.string.unit_lbs)
        }
        when (temp) {
            1 -> {
                valueWeight = if (isSetting) "1" else "2.2"
                des = "(-$valueWeight$unit/" + context!!.getString(R.string.all_week)
                    .lowercase(
                        Locale.getDefault()
                    ) + ")"
                binding!!.viewGoalTitle.text =
                    context!!.getString(R.string.onboarding_strict_loos)
                binding!!.viewGoalDes.text = des
                binding!!.viewGoalDes.visibility = VISIBLE
            }
            2 -> {
                valueWeight = if (isSetting) "0.5" else "1.1"
                des = "(-$valueWeight$unit/" + context!!.getString(R.string.all_week)
                    .lowercase(
                        Locale.getDefault()
                    ) + ")"
                binding!!.viewGoalTitle.text =
                    context!!.getString(R.string.onboarding_mormal_weight)
                binding!!.viewGoalDes.text = des
                binding!!.viewGoalDes.visibility = VISIBLE
            }
            3 -> {
                valueWeight = if (isSetting) "0.25" else "0.55"
                des = "(-$valueWeight$unit/" + context!!.getString(R.string.all_week)
                    .lowercase(
                        Locale.getDefault()
                    ) + ")"
                binding!!.viewGoalTitle.text =
                    context!!.getString(R.string.onboarding_comfortable)
                binding!!.viewGoalDes.text = des
                binding!!.viewGoalDes.visibility = VISIBLE
            }
            4 -> {
                valueWeight = if (isSetting) "" else ""
                des = ""
                binding!!.viewGoalTitle.text =
                    context!!.getString(R.string.onboarding_maintain)
                binding!!.viewGoalDes.visibility = GONE
            }
            5 -> {
                valueWeight = if (isSetting) "0.5" else "1.1"
                des = "(+$valueWeight$unit/" + context!!.getString(R.string.all_week)
                    .lowercase(
                        Locale.getDefault()
                    ) + ")"
                binding!!.viewGoalTitle.text =
                    context!!.getString(R.string.onboarding_normal)
                binding!!.viewGoalDes.text = des
                binding!!.viewGoalDes.visibility = VISIBLE
            }
            6 -> {
                valueWeight = if (isSetting) "1" else "2.2"
                des = "(+$valueWeight$unit/" + context!!.getString(R.string.all_week)
                    .lowercase(
                        Locale.getDefault()
                    ) + ")"
                binding!!.viewGoalTitle.text =
                    context!!.getString(R.string.onboarding_strict)
                binding!!.viewGoalDes.text = des
                binding!!.viewGoalDes.visibility = VISIBLE
            }
        }
    }
}