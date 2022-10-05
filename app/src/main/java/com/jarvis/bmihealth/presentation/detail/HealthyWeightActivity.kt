@file:Suppress("DEPRECATION")

package com.jarvis.bmihealth.presentation.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ActivityHealthyWeightBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import com.jarvis.bmihealth.presentation.common.Constant
import com.jarvis.bmihealth.common.observe
import com.jarvis.design_system.toolbar.JxToolbar
import com.well.unitlibrary.UnitConverter
import com.well.unitlibrary.UnitConverter.formatDoubleToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HealthyWeightActivity :
    BaseActivity<ActivityHealthyWeightBinding, BaseViewModel>(ActivityHealthyWeightBinding::inflate) {

    private val viewModel: DetailViewModel by viewModels()

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        initView()
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, intent)
            }
        }

    private fun initView() {
        setOnClickView()
        viewModel.getProfile()
    }

    private fun setOnClickView() {
        this.binding.tvEdit.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, RegisterActivity::class.java)
            intent.putExtra(Constant.NEXT_SCREEN_TO_PROFILE, true)
            resultLauncher.launch(intent)
        }
    }

    override fun observeData() {
        super.observeData()

        observe(viewModel.profileUsers) {
            viewModel.updateDataView()
            updateView()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() {

        if (viewModel.isKmSetting) {
            binding.tvWeight.text =
                formatDoubleToString(viewModel.minWeight, 1) + " - " + formatDoubleToString(
                    viewModel.maxWeight,
                    1
                )
            binding.tvUnit.text = getString(R.string.unit_kg)
            if (viewModel.userWeight < viewModel.minWeight && viewModel.minWeight - viewModel.userWeight >= 0.1) {
                setStatusText(
                    viewModel.arrayListIndex[0],
                    viewModel.minWeight - viewModel.userWeight,
                    viewModel.isKmSetting
                )
            } else if (viewModel.maxWeight < viewModel.userWeight && viewModel.userWeight - viewModel.maxWeight >= 0.1) {
                setStatusText(
                    viewModel.arrayListIndex[2],
                    viewModel.userWeight - viewModel.maxWeight,
                    viewModel.isKmSetting
                )
            } else {
                setStatusText(viewModel.arrayListIndex[1], 0.0, viewModel.isKmSetting)
            }
        } else {
            val minWeightLbs = UnitConverter.convertKgToLbs(viewModel.minWeight)
            val maxWeightLbs = UnitConverter.convertKgToLbs(viewModel.maxWeight)
            val userWeightLbs = UnitConverter.convertKgToLbs(viewModel.userWeight)
            binding.tvWeight.text =
                formatDoubleToString(minWeightLbs, 1) + " - " + formatDoubleToString(
                    maxWeightLbs,
                    1
                )
            binding.tvUnit.text = getString(R.string.unit_lbs)
            if (userWeightLbs < minWeightLbs && minWeightLbs - userWeightLbs >= 0.1) {
                setStatusText(
                    viewModel.arrayListIndex[0],
                    minWeightLbs - userWeightLbs,
                    viewModel.isKmSetting
                )
            } else if (maxWeightLbs < userWeightLbs && userWeightLbs - maxWeightLbs >= 0.1) {
                setStatusText(
                    viewModel.arrayListIndex[2],
                    userWeightLbs - maxWeightLbs,
                    viewModel.isKmSetting
                )
            } else {
                setStatusText(viewModel.arrayListIndex[1], 0.0, viewModel.isKmSetting)
            }
        }
    }

    private fun setStatusText(index: Int, weight: Double, isMeTric: Boolean) {
        val unit: String
        val weightUnit: Double
        if (isMeTric) {
            unit = getString(R.string.unit_kg)
            weightUnit = weight
        } else {
            unit = getString(R.string.unit_lbs)
            weightUnit = weight
        }
        this.binding.tvStatus.text = getString(viewModel.arrayListStatus[index])
        this.binding.tvStatus.setTextColor(
            ContextCompat.getColor(
                this,
                viewModel.arrayListStatusColor[index]
            )
        )
        this.binding.tvStatusDes.text = Html.fromHtml(
            getString(
                viewModel.arrayListStatusDes[index],
                formatDoubleToString(weightUnit, 1),
                unit
            )
        )
    }
}
