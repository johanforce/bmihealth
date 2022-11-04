package com.jarvis.bmihealth.presentation.heartrate.diary

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.common.extensions.setTextColorRes
import com.jarvis.bmihealth.common.utils.TimeUtil
import com.jarvis.bmihealth.common.utils.daysOfWeek
import com.jarvis.bmihealth.databinding.ExampleCalendarDayBinding
import com.jarvis.bmihealth.databinding.FragmentDiaryWeekBinding
import com.jarvis.bmihealth.domain.model.model.HeartRateModel
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class DiaryWeekFragment :
    BaseFragment<FragmentDiaryWeekBinding>(FragmentDiaryWeekBinding::inflate) {

    private val viewModel: HeartRateDiaryViewModel by viewModels()

    private var selectedDate: LocalDate? = LocalDate.now()

    private var lastDayOfSelectedWeek: LocalDate? = null
    private var firstDayOfSelectedWeek: LocalDate? = null
    private val today = LocalDate.now()
    private val currentMonth = YearMonth.now()
    private val lastEstDayOfMonth = YearMonth.now().atEndOfMonth()
    private var daySummaries: List<HeartRateModel?>? = null
    private lateinit var diaryHistoryAdapter: DiaryHistoryAdapter

    override fun setUpViews() {
        binding.lifecycleOwner = this

        initData()
    }

    private fun initData() {
        viewModel.getProfile()
        initCalender()
        initRecycleHistory()
        executePreviousWeek()
        executeNextWeek()
        executeTitleCalendar()
    }

    override fun observeData() {
        super.observeData()

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
            binding.cvWeek.notifyDateChanged(
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            )
        }
    }

    private fun initCalender() {
        val daysOfWeek = daysOfWeek(requireContext())
        binding.cvWeek.setup(currentMonth.minusYears(15), YearMonth.now(), daysOfWeek.first())
        binding.cvWeek.scrollToDate(today)
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = ExampleCalendarDayBinding.bind(view).exOneDayText
            val bgData = ExampleCalendarDayBinding.bind(view).bgData

            init {
                view.setOnClickListener {
                    if (selectedDate == day.date) {
                        getDataHistory()
                        binding.cvWeek.notifyDayChanged(day)
                    } else {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding.cvWeek.notifyDateChanged(day.date)
                        oldDate?.let { binding.cvWeek.notifyDateChanged(oldDate) }
                        getDataHistory()
                    }
                    binding.cvWeek.notifyDayChanged(day)
                }
            }
        }

        binding.cvWeek.dayBinder = object : DayBinder<DayViewContainer> {
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
            }
        }
    }

    private fun executeTitleCalendar() {
        val headerDateFormatter = TimeUtil.getFormatShortMonthDay(context)
        binding.cvWeek.monthScrollListener = { month ->
            firstDayOfSelectedWeek = month.weekDays.first().first().date
            lastDayOfSelectedWeek = month.weekDays.first().last().date
            val firstDay = headerDateFormatter?.format(firstDayOfSelectedWeek)
            val lastDay = headerDateFormatter?.format(lastDayOfSelectedWeek)
            val title = "$firstDay - $lastDay"
            binding.layoutTitle.tvCalendar.text = title

            binding.layoutTitle.btCalendarNext.isEnabled =
                lastDayOfSelectedWeek!!.isBefore(lastEstDayOfMonth)
            getDataHistory()
        }
    }

    private fun getDataHistory() {
        viewModel.getHeartRateInTime(
            Date.from(
                firstDayOfSelectedWeek?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
            ).time,
            Date.from(
                lastDayOfSelectedWeek?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
            ).time
        )
    }

    private fun executePreviousWeek() {
        selectedDate = selectedDate?.minusDays(7)
        binding.layoutTitle.btCalendarBack.setOnClickListener {
            binding.cvWeek.findFirstVisibleDay()?.let {
                binding.cvWeek.smoothScrollToDate(it.date.minusWeeks(1))
            }
        }
    }

    private fun executeNextWeek() {
        selectedDate = selectedDate?.plusDays(7)
        binding.layoutTitle.btCalendarNext.setOnClickListener {
            binding.cvWeek.findFirstVisibleDay()?.let {
                binding.cvWeek.smoothScrollToDate(it.date.plusWeeks(1))
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
