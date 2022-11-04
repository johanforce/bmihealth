package com.jarvis.bmihealth.presentation.heartrate.diary

import android.app.Activity
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.enums.HeartRateEnum
import com.jarvis.bmihealth.common.utils.TimeUtil
import com.jarvis.bmihealth.databinding.AdapterExerciseDiaryTitleBinding
import com.jarvis.bmihealth.databinding.ItemRecentActivityBinding
import com.jarvis.bmihealth.domain.model.model.HeartRateModel
import java.util.*

class DiaryHistoryAdapter(private val context: FragmentActivity?, activity: Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listHeartRate: MutableList<HeartRateModel?>?
    private val activity: Activity
    private var viewEmptyListener: UpdateViewEmptyListenner? = null

    fun addData(exerciseSummaries: List<HeartRateModel?>) {
        if (listHeartRate == null) {
            listHeartRate = ArrayList()
        }
        listHeartRate!!.clear()
        listHeartRate!!.addAll(exerciseSummaries)
        if (viewEmptyListener != null) {
            viewEmptyListener!!.onUpdateViewEmpty(exerciseSummaries)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val binding =
                AdapterExerciseDiaryTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderTitle(binding)
        }
        val billing =
            ItemRecentActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(billing)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        if (itemViewType == 1) {
            (holder as ViewHolderTitle).bindView(
                listHeartRate!![position], position
            )
            return
        }

        var showDivider = false
        if (position - 1 > 0) {
            val heartRateSummary = listHeartRate!![position - 1]
            showDivider = heartRateSummary?.id == 0
        }
        (holder as ViewHolder).bindView(
            listHeartRate!![position], showDivider
        )
    }

    override fun getItemCount(): Int {
        return listHeartRate!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val heartRateSummary = listHeartRate!![position]
        return if (heartRateSummary?.id == 0) {
            1
        } else 0
    }

    interface UpdateViewEmptyListenner {
        fun onUpdateViewEmpty(listExerciseSummary: List<HeartRateModel?>?)
    }

    class ViewHolderTitle(itemView: AdapterExerciseDiaryTitleBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val binding: AdapterExerciseDiaryTitleBinding?
        fun bindView(heartRateModel: HeartRateModel?, position: Int) {
            if (binding == null || heartRateModel == null) {
                return
            }
            if (position == 0) {
                binding.line.visibility = View.GONE
            }
            val calendar = Calendar.getInstance()
            val longTime = heartRateModel.dateTime
            calendar.timeInMillis = longTime
            var displayName =
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            if (displayName != null) {
                displayName = displayName.uppercase(Locale.getDefault())
            }
            val strDate = TimeUtil.formatDateMonthDayYear(longTime)
                .uppercase(Locale.getDefault())
            val strTitle = "$displayName | $strDate"
            binding.tvTitle.text = strTitle
        }

        init {
            binding = itemView
        }
    }

    inner class ViewHolder(itemView: ItemRecentActivityBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val binding: ItemRecentActivityBinding?
        fun bindView(heartRateModel: HeartRateModel?, showDivider: Boolean) {
            if (heartRateModel == null || binding == null) {
                return
            }

            if (showDivider) {
                binding.viewLine.visibility = View.VISIBLE
            }

            val imageData = when (heartRateModel.activity) {
                HeartRateEnum.GENERAL.activity -> R.drawable.ic_general_on
                HeartRateEnum.RELAX.activity -> R.drawable.ic_relax_on
                HeartRateEnum.WORKING.activity -> R.drawable.ic_working_on
                HeartRateEnum.WAKE_UP.activity -> R.drawable.ic_wake_up_on
                HeartRateEnum.TIRED.activity -> R.drawable.ic_before_on
                HeartRateEnum.BEFORE.activity -> R.drawable.ic_before_on
                else -> R.drawable.ic_general_on
            }
            val stringData = when (heartRateModel.activity) {
                HeartRateEnum.GENERAL.activity -> R.string.heart_rate_general
                HeartRateEnum.RELAX.activity -> R.string.heart_rate_relax
                HeartRateEnum.WORKING.activity -> R.string.heart_rate_working
                HeartRateEnum.WAKE_UP.activity -> R.string.heart_rate_wake_up
                HeartRateEnum.TIRED.activity -> R.string.heart_rate_tired
                HeartRateEnum.BEFORE.activity -> R.string.heart_rate_before
                else -> R.string.heart_rate_general
            }

            binding.ivStartIcon.setImageResource(imageData)
            binding.tvTitle.text = context?.getString(stringData)
            binding.tvBPM.text = context?.getString(
                R.string.heart_rate_result_diary,
                heartRateModel.heartRate.toString()
            )
            val dateString =
                DateFormat.format("hh:mm AA", Date(heartRateModel.dateTime))
                    .toString()
            binding.tvStartTime.text = dateString
        }

        init {
            binding = itemView
        }
    }

    init {
        listHeartRate = ArrayList()
        this.activity = activity
    }
}
