package com.jarvis.bmihealth.presentation.register

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ViewGoalEditBinding
import com.jarvis.bmihealth.presentation.pref.AppPreference

class ViewGoalEdit : ConstraintLayout {
    var binding: ViewGoalEditBinding? = null
    private lateinit var appPreference: AppPreference
    private var mGoalAdapter: GoalAdapter = GoalAdapter()
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    var onListenerPosition: OnListenerPosition? = null
    private var firstPositionItemGoal: Int = 3
    private var lastPositionItemGoal: Int = 5

    constructor(context: Context) :
            super(context, null, 0, 0) {
    }

    fun setListenerPosition(onListenerPosition: OnListenerPosition) {
        this.onListenerPosition = onListenerPosition
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
        0
    ) {
    }

    fun initView(context: Context, boolean: Boolean, index: Int) {
        appPreference = AppPreference.getInstance()
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
                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastPositionItemGoal >= firstPositionItemGoal + 1) {
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
        binding?.rcGoal?.scrollToPosition(index-1)
    }

    fun setDataChoose(index: Int) {
        binding?.rcGoal?.scrollToPosition(index)
    }

    fun getListGoal(isMetric: Boolean): MutableList<GoalModel> {
        val listGoal: MutableList<GoalModel> = ArrayList()
        val unit =
            if (isMetric) context.getString(R.string.unit_kg) else context.getString(R.string.unit_lbs)

        val modelTab1 = GoalModel()
        val valueLossWeight = if (isMetric) "1" else "2.2"
        modelTab1.title = context.getString(R.string.onboarding_strict_loos)
        modelTab1.des = "- " + valueLossWeight.plus(" ").plus(unit).plus(" / ")
            .plus(context.getString(R.string.all_week).lowercase())
        listGoal.add(modelTab1)

        val modelTab3 = GoalModel()
        modelTab3.title = context.getString(R.string.onboarding_mormal_weight)
        val valueMormalLossWeight = if (isMetric) "0.5" else "1.1"
        modelTab3.des = "- " + valueMormalLossWeight.plus(" ").plus(unit).plus(" / ")
            .plus(context.getString(R.string.all_week).lowercase())
        listGoal.add(modelTab3)

        val modelTab4 = GoalModel()
        modelTab4.title = context.getString(R.string.onboarding_comfortable)
        val value4 = if (isMetric) "0.25" else "0.55"
        modelTab4.des =
            "- " + value4.plus(" ").plus(unit).plus(" / ")
                .plus(context.getString(R.string.all_week).lowercase())
        listGoal.add(modelTab4)

        val modelTab2 = GoalModel()
        modelTab2.title = context.getString(R.string.onboarding_maintain)
        listGoal.add(modelTab2)


        val modelTab5 = GoalModel()
        modelTab5.title = context.getString(R.string.onboarding_normal)
        val value5 = if (isMetric) "0.5" else "1.1"
        modelTab5.des = "+ " + value5.plus(" ").plus(unit).plus(" / ")
            .plus(context.getString(R.string.all_week).lowercase())
        listGoal.add(modelTab5)

        val modelTab6 = GoalModel()
        modelTab6.title = context.getString(R.string.onboarding_strict)
        val value6 = if (isMetric) "1" else "2.2"
        modelTab6.des = "+ " + value6.plus(" ").plus(unit).plus(" / ")
            .plus(context.getString(R.string.all_week).lowercase())
        listGoal.add(modelTab6)
        return listGoal
    }

    interface OnListenerPosition {
        fun positionItem(position: Int)
    }
}