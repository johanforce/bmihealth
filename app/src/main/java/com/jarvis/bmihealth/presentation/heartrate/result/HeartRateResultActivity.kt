@file:Suppress("DEPRECATION")

package com.jarvis.bmihealth.presentation.heartrate.result

import android.os.Bundle
import androidx.activity.viewModels
import com.jarvis.bmihealth.common.enums.HeartRateEnum
import com.jarvis.bmihealth.common.extensions.click
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.common.utils.TimeUtil
import com.jarvis.bmihealth.databinding.ActivityHeartRateResultBinding
import com.jarvis.bmihealth.domain.model.model.HeartRateModel
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.common.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeartRateResultActivity :
    BaseActivity<ActivityHeartRateResultBinding, HeartRateResultViewModel>(
        ActivityHeartRateResultBinding::inflate
    ) {

    private val viewModel: HeartRateResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    override fun setUpViews() {
        super.setUpViews()
        getIntentData()
        initData()
    }

    private fun initData() {
        setOnClickView()
        initView()
    }

    private fun initView() {
        binding.tvHeartRate.text = viewModel.heartRateModel.heartRate.toString()
        binding.desDay.text = TimeUtil.formatDateMonthDayYear(viewModel.heartRateModel.dateTime)
        binding.desTime.text = TimeUtil.formatTimeHourMinute(viewModel.heartRateModel.dateTime)
    }

    private fun getIntentData() {
        viewModel.heartRateModel =
            intent?.getParcelableExtra(Constant.HEART_RATE_RESULT) ?: HeartRateModel()
    }

    private fun setOnClickView() {
        binding.ivGeneral.click {
            clickActivityView(HeartRateEnum.GENERAL.activity)
        }
        binding.ivTired.click {
            clickActivityView(HeartRateEnum.TIRED.activity)
        }
        binding.ivRelax.click {
            clickActivityView(HeartRateEnum.RELAX.activity)
        }
        binding.ivBeforeWorkout.click {
            clickActivityView(HeartRateEnum.BEFORE.activity)
        }
        binding.ivWakeUp.click {
            clickActivityView(HeartRateEnum.WAKE_UP.activity)
        }
        binding.ivWorking.click {
            clickActivityView(HeartRateEnum.WORKING.activity)
        }

        binding.btStart.click {
            viewModel.heartRateModel.apply {
                activity = viewModel.activityLevelHeartRate
                note = binding.edtNote.text.toString()
            }
            viewModel.addHeartRate()
        }
    }

    private fun clickActivityView(activity: Int) {
        binding.ivGeneral.isSelected = HeartRateEnum.GENERAL.activity == activity
        binding.ivTired.isSelected = HeartRateEnum.TIRED.activity == activity
        binding.ivRelax.isSelected = HeartRateEnum.RELAX.activity == activity
        binding.ivBeforeWorkout.isSelected = HeartRateEnum.BEFORE.activity == activity
        binding.ivWakeUp.isSelected = HeartRateEnum.WAKE_UP.activity == activity
        binding.ivWorking.isSelected = HeartRateEnum.WORKING.activity == activity
    }


    override fun observeData() {
        super.observeData()

        observe(viewModel.isSaveSuccess) {
            viewModel.isSaveSuccess.value = null
            finish()
        }
    }
}


