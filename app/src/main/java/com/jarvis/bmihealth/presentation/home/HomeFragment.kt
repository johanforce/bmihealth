package com.jarvis.bmihealth.presentation.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.FragmentHomeBinding
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.jarvis.bmihealth.presentation.main.MainViewModel
import com.jarvis.bmihealth.presentation.utilx.DeviceUtil
import com.jarvis.bmihealth.presentation.utilx.TypeUnit.Companion.METRIC
import com.jarvis.bmihealth.presentation.utilx.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment(context: AppCompatActivity) :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by context.viewModels()
    private val mainViewModel: MainViewModel by context.viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initData()
        setOnClickView()
    }

    private fun setOnClickView() {
        binding.tvBodyIndex?.setOnClickListener {
            viewModel.isShowHealthIndex.value = false
        }

        binding.tvHealIndex?.setOnClickListener {
            viewModel.isShowHealthIndex.value = true
        }
    }

    private fun initData() {

    }

    override fun observeData() {
        observe(mainViewModel.profileUsers) {
            mainViewModel.profileUser = it.firstOrNull() ?: ProfileUserModel()
            mainViewModel.isKmSetting = mainViewModel.profileUser.unit == METRIC
            binding.viewBMI.progressBMI.setCurrentValues(mainViewModel.getBMI().toFloat())
            val healthyWeight = mainViewModel.getHealthyWeight()
            "${DeviceUtil.roundOffDecimal(healthyWeight.healthyWeightTo)} - ${
                DeviceUtil.roundOffDecimal(
                    healthyWeight.healthyWeightFrom
                )
            }".also { it -> binding.viewBMI.tvWeight.text = it }
            binding.viewBMI.tvUnit.text =
                if (mainViewModel.isKmSetting) getText(R.string.unit_kg) else getText(R.string.unit_lbs)
            binding.viewBMI.tvBodyPercent.text = DeviceUtil.roundOffDecimal(mainViewModel.getBodyFat())
            binding.itemBMR?.setDataView(mainViewModel.getBMR().toDouble(), true)
            binding.itemSaveWeight?.setDataView(mainViewModel.getCalories().toDouble(), true)
            binding.itemHeat?.setDataView(90.0, true)
            binding.itemHeight?.setDataView(
               mainViewModel.profileUser.height,
                false
            )
            binding.itemWeight?.setDataView(
                mainViewModel.profileUser.weight,
                false
            )
            binding.itemAge?.setDataView(23.0, true)
        }

        observe(viewModel.isShowHealthIndex) {
            viewModel.isShowHealthIndex.value = null
            binding.listBody?.isVisible = !it
            binding.listHealth?.isVisible = it
            if (it) {
                binding.tvBodyIndex?.background = null
                binding.tvHealIndex?.background =
                    activity?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1,
                            R.drawable.shape_bg_tabitem
                        )
                    }
            } else {
                binding.tvHealIndex?.background = null
                binding.tvBodyIndex?.background =
                    activity?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1,
                            R.drawable.shape_bg_tabitem
                        )
                    }
            }
        }
    }

}

