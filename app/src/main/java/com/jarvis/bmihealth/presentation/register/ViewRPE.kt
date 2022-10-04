@file:Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate", "UNUSED_VALUE")

package com.jarvis.bmihealth.presentation.register

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ViewRpeBinding
import com.jarvis.bmihealth.presentation.utilx.ActivityEnum
import com.jarvis.bmihealth.presentation.utilx.GoalEnum
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams

class ViewRPE : ConstraintLayout {
    private var binding: ViewRpeBinding? = null
    private var isKmSetting = false
    private var stringTitle: String? = null
    private var stringDes: String? = null
    private var listenerClick: ListenerClick? = null
    var goalTemp = GoalEnum.MAINTAIN_WEIGHT.index
    var activityTemp = ActivityEnum.MODERATELY.index

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
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewRpeBinding.inflate(layoutInflater, this, true)
        activityTemp = activity + 1
        goalTemp = goal
        isKmSetting= isSetting

        setActivityLevel(activityTemp)
        updateRPE(activityTemp)

        binding!!.sbEffortNew.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                if (seekParams.thumbPosition < 1) {
                    binding!!.sbEffortNew.setProgress(1f)
                    updateRPE(0)
                    activityTemp = 0
                } else {
                    activityTemp = seekParams.thumbPosition-1
                    updateRPE(seekParams.thumbPosition)
                }
                if (listenerClick != null) {
                    listenerClick!!.dataActivityLevel(activityTemp)
                }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {
                //do nothing
            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
                //do nothing
            }
        }

        setTextGoal(goalTemp, isKmSetting)
        binding?.viewGoal?.setOnListenerClick {
            val dialogSetGoal = DialogSetGoal(context, goalTemp, isKmSetting)
            dialogSetGoal.show()
            dialogSetGoal.onListener(object : DialogSetGoal.ListenerClick {
                override fun closeDialog(isClickBackgroud: Boolean) {
                    //do nothing
                }

                override fun saveGoal(goal: Int) {
                    if (listenerClick != null) {
                        listenerClick?.dataGoal(goal)
                    }
                    goalTemp = goal
                    setTextGoal(goal, isKmSetting)
                }
            })
        }
    }

    private fun progressToLevel(progress: Int): Int {
        return if (progress < 2) {
            ActivityEnum.SEDENTARY.index
        } else if (progress < 4) {
            ActivityEnum.SEDENTARY.index
        } else if (progress < 6) {
            ActivityEnum.LIGHT_ACTIVE.index
        } else if (progress < 8) {
            ActivityEnum.MODERATELY.index
        } else if (progress < 10) {
            ActivityEnum.VERY_ACTIVE.index
        } else {
            ActivityEnum.EXTREMELY_ACTIVE.index
        }
    }

    fun updateRPE(rpe: Int) {
        when (rpe) {
            0 -> {
                stringTitle = context.getString(R.string.rpe_sedentary)
                stringDes = context.getString(R.string.rpe_sedentary_des)
                setValueRPE(stringTitle, stringDes)
            }
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
                override fun closeDialog(isClickBackgroud: Boolean) {
                    //do nothing
                }

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
        binding!!.viewGoalTitle.text =
            when (temp) {
                GoalEnum.WEIGHT_LOSS_III.index -> GoalEnum.WEIGHT_LOSS_III.title
                GoalEnum.WEIGHT_LOSS_II.index -> GoalEnum.WEIGHT_LOSS_II.title
                GoalEnum.WEIGHT_LOSS_I.index -> GoalEnum.WEIGHT_LOSS_I.title
                GoalEnum.MAINTAIN_WEIGHT.index -> {
                    GoalEnum.MAINTAIN_WEIGHT.title
                }
                GoalEnum.WEIGHT_GAIN_I.index -> GoalEnum.WEIGHT_GAIN_I.title
                GoalEnum.WEIGHT_GAIN_II.index -> GoalEnum.WEIGHT_GAIN_II.title
                else -> GoalEnum.MAINTAIN_WEIGHT.title
            }

        binding!!.viewGoalDes.text =
            if (isSetting)
                when (temp) {
                    GoalEnum.WEIGHT_LOSS_III.index -> GoalEnum.WEIGHT_LOSS_III.desMetric
                    GoalEnum.WEIGHT_LOSS_II.index -> GoalEnum.WEIGHT_LOSS_II.desMetric
                    GoalEnum.WEIGHT_LOSS_I.index -> GoalEnum.WEIGHT_LOSS_I.desMetric
                    GoalEnum.MAINTAIN_WEIGHT.index -> GoalEnum.MAINTAIN_WEIGHT.desMetric
                    GoalEnum.WEIGHT_GAIN_I.index -> GoalEnum.WEIGHT_GAIN_I.desMetric
                    GoalEnum.WEIGHT_GAIN_II.index -> GoalEnum.WEIGHT_GAIN_II.desMetric
                    else -> GoalEnum.MAINTAIN_WEIGHT.desMetric
                }
            else
                when (temp) {
                    GoalEnum.WEIGHT_LOSS_III.index -> GoalEnum.WEIGHT_LOSS_III.desImperial
                    GoalEnum.WEIGHT_LOSS_II.index -> GoalEnum.WEIGHT_LOSS_II.desImperial
                    GoalEnum.WEIGHT_LOSS_I.index -> GoalEnum.WEIGHT_LOSS_I.desImperial
                    GoalEnum.MAINTAIN_WEIGHT.index -> GoalEnum.MAINTAIN_WEIGHT.desImperial
                    GoalEnum.WEIGHT_GAIN_I.index -> GoalEnum.WEIGHT_GAIN_I.desImperial
                    GoalEnum.WEIGHT_GAIN_II.index -> GoalEnum.WEIGHT_GAIN_II.desImperial
                    else -> GoalEnum.MAINTAIN_WEIGHT.desImperial
                }
    }
}
