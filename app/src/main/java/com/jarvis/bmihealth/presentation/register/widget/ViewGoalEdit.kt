package com.jarvis.bmihealth.presentation.register.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.bmihealth.databinding.ViewGoalEditBinding
import com.jarvis.bmihealth.common.enums.GoalEnum
import com.jarvis.bmihealth.presentation.register.adapter.GoalAdapter
import com.jarvis.bmihealth.presentation.register.model.GoalModel

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ViewGoalEdit : ConstraintLayout {
    var binding: ViewGoalEditBinding? = null

    private var mGoalAdapter: GoalAdapter = GoalAdapter()
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    var onListenerPosition: OnListenerPosition? = null
    private var firstPositionItemGoal: Int = 3
    private var lastPositionItemGoal: Int = 5

    constructor(context: Context) : super(context, null, 0, 0)

    fun setListenerPosition(onListenerPosition: OnListenerPosition) {
        this.onListenerPosition = onListenerPosition
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
        0
    )

    fun initView(context: Context, boolean: Boolean, index: Int) {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewGoalEditBinding.inflate(layoutInflater, this, true)
        this.mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.rcGoal?.layoutManager = mLayoutManager

        val listData: MutableList<GoalModel> = ArrayList()
        listData.add(0, GoalModel())
        listData.addAll(getListGoal(boolean))
        listData.add(GoalModel())

        mGoalAdapter.updateData(listData)
        binding?.rcGoal?.adapter = mGoalAdapter

        binding?.rcGoal?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPositionItemGoal >= firstPositionItemGoal + 1) {
                    onListenerPosition?.positionItem((firstPositionItemGoal + lastPositionItemGoal) / 2)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                firstPositionItemGoal =
                    (mLayoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                lastPositionItemGoal =
                    (mLayoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            }
        })
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding?.rcGoal)
        binding?.rcGoal?.scrollToPosition(index)
    }

    fun setDataChoose(index: Int) {
        binding?.rcGoal?.scrollToPosition(index)
    }

    fun getListGoal(isMetric: Boolean): MutableList<GoalModel> {
        val listGoal = mutableListOf<GoalModel>()
        if (isMetric) {
            listGoal.add(
                GoalModel(
                    GoalEnum.WEIGHT_LOSS_III.title,
                    GoalEnum.WEIGHT_LOSS_III.desMetric
                )
            )
            listGoal.add(
                GoalModel(
                    GoalEnum.WEIGHT_LOSS_II.title,
                    GoalEnum.WEIGHT_LOSS_II.desMetric
                )
            )
            listGoal.add(GoalModel(GoalEnum.WEIGHT_LOSS_I.title, GoalEnum.WEIGHT_LOSS_I.desMetric))
            listGoal.add(
                GoalModel(
                    GoalEnum.MAINTAIN_WEIGHT.title,
                    GoalEnum.MAINTAIN_WEIGHT.desMetric
                )
            )
            listGoal.add(GoalModel(GoalEnum.WEIGHT_GAIN_I.title, GoalEnum.WEIGHT_GAIN_I.desMetric))
            listGoal.add(
                GoalModel(
                    GoalEnum.WEIGHT_GAIN_II.title,
                    GoalEnum.WEIGHT_GAIN_II.desMetric
                )
            )
        } else {
            listGoal.add(
                GoalModel(
                    GoalEnum.WEIGHT_LOSS_III.title,
                    GoalEnum.WEIGHT_LOSS_III.desImperial
                )
            )
            listGoal.add(
                GoalModel(
                    GoalEnum.WEIGHT_LOSS_II.title,
                    GoalEnum.WEIGHT_LOSS_II.desImperial
                )
            )
            listGoal.add(
                GoalModel(
                    GoalEnum.WEIGHT_LOSS_I.title,
                    GoalEnum.WEIGHT_LOSS_I.desImperial
                )
            )
            listGoal.add(
                GoalModel(
                    GoalEnum.MAINTAIN_WEIGHT.title,
                    GoalEnum.MAINTAIN_WEIGHT.desImperial
                )
            )
            listGoal.add(
                GoalModel(
                    GoalEnum.WEIGHT_GAIN_I.title,
                    GoalEnum.WEIGHT_GAIN_I.desImperial
                )
            )
            listGoal.add(
                GoalModel(
                    GoalEnum.WEIGHT_GAIN_II.title,
                    GoalEnum.WEIGHT_GAIN_II.desImperial
                )
            )
        }
        return listGoal
    }

    interface OnListenerPosition {
        fun positionItem(position: Int)
    }
}
