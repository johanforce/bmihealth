package com.jarvis.bmihealth.presentation.heartrate.diary

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.extensions.next
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.common.extensions.previous
import com.jarvis.bmihealth.common.extensions.setTextColorRes
import com.jarvis.bmihealth.common.utils.DeviceUtil
import com.jarvis.bmihealth.common.utils.daysOfWeek
import com.jarvis.bmihealth.databinding.ExampleCalendarDayBinding
import com.jarvis.bmihealth.databinding.FragmentDiaryMonthBinding
import com.jarvis.bmihealth.domain.model.model.HeartRateModel
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.jarvis.locale_helper.helper.LocaleHelper
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class DiaryMonthFragment :
    BaseFragment<FragmentDiaryMonthBinding>(FragmentDiaryMonthBinding::inflate) {

    private val viewModel: HeartRateDiaryViewModel by viewModels()


    private var selectedDate: LocalDate? = LocalDate.now()

    private var firstTime: Boolean = true
    private var displayMonth: YearMonth = YearMonth.now()
    private var diaryActivity: DiaryHeartRateActivity? = null
    private var lastDayOfSelectedMonth: LocalDate? = null
    private var firstDayOfSelectedMonth: LocalDate? = null
    private val today = LocalDate.now()
    private val currentMonth = YearMonth.now()

    private var daySummaries: List<HeartRateModel?>? = null
    private var daySummariesOld: List<HeartRateModel?>? = null

    private lateinit var diaryHistoryAdapter: DiaryHistoryAdapter

    override fun setUpViews() {
        binding.lifecycleOwner = this

        initData()
    }

    private fun initData() {
        this.diaryActivity = activity as DiaryHeartRateActivity
        viewModel.getProfile()
        initRecycleHistory()
        initCalender()
        executePreviousMonth()
        executeNextMonth()
        executeTitleCalendar()
    }

    override fun observeData() {
        observe(viewModel.listHeartRate) { list ->
            daySummaries = list
            val data = list.groupBy { it?.dateTime }.toList().sortedBy { it.first }.flatMap {
                listOf(
                    HeartRateModel(
                        dateTime = it.second[0]?.dateTime ?: 0L
                    )
                ) + it.second
            }
            diaryHistoryAdapter.addData(data)
            diaryHistoryAdapter.notifyDataSetChanged()
            executeRefreshViewDayWithDaySummary()
        }
    }

    private fun executeRefreshViewDayWithDaySummary() {
        daySummaries?.forEach {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it?.dateTime ?: 0L
            val date = calendar.time
            binding.cvMonth.notifyDateChanged(
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            )
        }
        daySummariesOld?.forEach {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it?.dateTime ?: 0L
            val date = calendar.time
            binding.cvMonth.notifyDateChanged(
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            )
        }
        daySummariesOld = daySummaries
    }


    private fun initCalender() {
        if (!firstTime) {
            return
        }
        firstTime = false
        val daysOfWeek = daysOfWeek(requireContext())
        binding.cvMonth.setup(currentMonth.minusYears(15), YearMonth.now(), daysOfWeek.first())
        binding.cvMonth.scrollToMonth(currentMonth)
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val rootDay = ExampleCalendarDayBinding.bind(view).root
            val textView = ExampleCalendarDayBinding.bind(view).exOneDayText
            val bgData = ExampleCalendarDayBinding.bind(view).bgData

            init {
                view.setOnClickListener {
                    if (selectedDate == day.date) {
                        getDataHistory()
                        binding.cvMonth.notifyDayChanged(day)
                    } else {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding.cvMonth.notifyDateChanged(day.date)
                        oldDate?.let { binding.cvMonth.notifyDateChanged(oldDate) }
                        getDataHistory()
                    }
                    binding.cvMonth.notifyDayChanged(day)
                }
            }
        }

        binding.cvMonth.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                val daySummary = daySummaries?.any {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = it?.dateTime ?: 0L

                    val temp = Date.from(day.date.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    val calTemp = Calendar.getInstance()
                    calTemp.time = temp

                    val yearDay = calendar.get(Calendar.YEAR)
                    val monthDay = calendar.get(Calendar.MONTH)
                    val dayDay = calendar.get(Calendar.DAY_OF_MONTH)

                    val yearTemp = calTemp.get(Calendar.YEAR)
                    val monthTemp = calTemp.get(Calendar.MONTH)
                    val dayTemp = calTemp.get(Calendar.DAY_OF_MONTH)
                    (yearDay == yearTemp) && (monthDay == monthTemp) && (dayDay == dayTemp)
                }

                if (daySummary == true) container.bgData.setBackgroundResource(R.drawable.bg_boder_pri_3)
                else container.bgData.setBackgroundResource(0)

                if (day.owner == DayOwner.THIS_MONTH) {
                    container.rootDay.isEnabled = true
                    container.rootDay.visibility = View.VISIBLE
                    when (day.date) {
                        selectedDate -> {
                            if (day.date == today) {
                                textView.setBackgroundResource(R.drawable.bg_boder_i5)
                                textView.setTextColorRes(R.color.pri_1)
                            } else {
                                textView.setBackgroundResource(R.drawable.bg_boder_i5)
                            }
                        }
                        today -> {
                            textView.setTextColorRes(R.color.pri_1)
                            textView.background = null
                        }
                        else -> {
                            textView.setTextColorRes(R.color.ink_5)
                            textView.background = null
                        }
                    }
                } else {
                    container.rootDay.isEnabled = false
                    container.rootDay.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun executeTitleCalendar() {
        val local = LocaleHelper.getInstance().getLocale(context)

        binding.cvMonth.monthScrollListener = { month ->
            daySummariesOld = null
            displayMonth = month.yearMonth
            firstDayOfSelectedMonth = month.yearMonth.atDay(1)
            lastDayOfSelectedMonth = month.yearMonth.atEndOfMonth()
            val monthName = DeviceUtil.upCaseFirst(
                month.yearMonth.month.getDisplayName(
                    TextStyle.FULL,
                    local
                )
            )
            val title = "${monthName}, ${month.year}"
            binding.layoutTitle.tvCalendar.text = title
            binding.layoutTitle.btCalendarNext.isEnabled = month.yearMonth != currentMonth
            getDataHistory()
        }
    }

    private fun getDataHistory() {
        viewModel.getHeartRateInTime(
            Date.from(
                firstDayOfSelectedMonth?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
            ).time,
            Date.from(
                lastDayOfSelectedMonth?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
            ).time
        )
    }

    private fun executePreviousMonth() {
        selectedDate = selectedDate?.minusMonths(1)
        binding.layoutTitle.btCalendarBack.setOnClickListener {
            binding.cvMonth.findFirstVisibleMonth()?.let {
                binding.cvMonth.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }

    private fun executeNextMonth() {
        selectedDate = selectedDate?.plusMonths(1)
        binding.layoutTitle.btCalendarNext.setOnClickListener {
            binding.cvMonth.findFirstVisibleMonth()?.let {
                binding.cvMonth.smoothScrollToMonth(it.yearMonth.next)
            }
        }
    }

    private fun initRecycleHistory() {
        this.diaryHistoryAdapter = DiaryHistoryAdapter(activity, requireActivity())
        binding.recycleHistory.layoutManager = LinearLayoutManager(context)
        binding.recycleHistory.isNestedScrollingEnabled = false
        binding.recycleHistory.adapter = diaryHistoryAdapter
    }
}
