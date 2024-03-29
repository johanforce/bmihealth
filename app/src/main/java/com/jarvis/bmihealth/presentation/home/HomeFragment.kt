package com.jarvis.bmihealth.presentation.home

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.enums.ActivityEnum
import com.jarvis.bmihealth.common.enums.TypeUnits
import com.jarvis.bmihealth.common.extensions.click
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.databinding.FragmentHomeBinding
import com.jarvis.bmihealth.domain.model.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.jarvis.bmihealth.presentation.bmiother.ResultOtherActivity
import com.jarvis.bmihealth.presentation.detail.BmiUserIndexActivity
import com.jarvis.bmihealth.presentation.detail.BmrUserActivity
import com.jarvis.bmihealth.presentation.detail.CaloriesRequiredActivity
import com.jarvis.bmihealth.presentation.detail.HealthyWeightActivity
import com.jarvis.bmihealth.presentation.heartrate.StartHeartRateActivity
import com.jarvis.bmihealth.presentation.home.widget.ViewHomeBMI
import com.jarvis.bmihealth.presentation.home.widget.ViewInputDataHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun setUpViews() {
        binding.lifecycleOwner = this

        initData()
        setOnClickView()
    }

    private fun initData() {
        viewModel.getProfile()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            viewModel.getProfile()
        }
    }

    private fun setOnClickView() {
        binding.tvBodyIndex.click {
            viewModel.isShowHealthIndex.value = false
        }

        binding.tvHealIndex.click {
            viewModel.isShowHealthIndex.value = true
        }

        binding.itemBMR.setOnClickViewListener(object : ViewInputDataHome.OnClickProfileListener {
            override fun clickView() {
                val intent = Intent()
                context?.let { it1 -> intent.setClass(it1, BmrUserActivity::class.java) }
                resultLauncher.launch(intent)
            }
        })

        binding.itemHeat.setOnClickViewListener(object : ViewInputDataHome.OnClickProfileListener {
            override fun clickView() {
                val intent = Intent()
                context?.let { it1 -> intent.setClass(it1, StartHeartRateActivity::class.java) }
                resultLauncher.launch(intent)
            }
        })

        binding.itemSaveWeight.setTitle(
            viewModel.updateTitleCalories(
                viewModel.profileUser.activityLevel ?: ActivityEnum.MODERATELY.index
            )
        )
        binding.itemSaveWeight.setOnClickViewListener(object :
            ViewInputDataHome.OnClickProfileListener {
            override fun clickView() {
                val intent = Intent()
                context?.let { it1 -> intent.setClass(it1, CaloriesRequiredActivity::class.java) }
                resultLauncher.launch(intent)
            }
        })

        binding.viewHomeBMI.setOnClick(object : ViewHomeBMI.OnClickListener {
            override fun onClickHealthy() {
                val intent = Intent()
                context?.let { it1 -> intent.setClass(it1, HealthyWeightActivity::class.java) }
                resultLauncher.launch(intent)
            }

            override fun onClickBody() {
                TODO("Not yet implemented")
            }

            override fun onClickBMI() {
                val intent = Intent()
                activity?.let { it1 -> intent.setClass(it1, BmiUserIndexActivity::class.java) }
                resultLauncher.launch(intent)
            }

        })

        binding.ivProfile.click {
            val intent = Intent()
            activity?.let { it1 -> intent.setClass(it1, ResultOtherActivity::class.java) }
            resultLauncher.launch(intent)
        }
    }

    override fun observeData() {
        observe(viewModel.profileUsers) { profiles ->
            viewModel.profileUser = profiles.firstOrNull() ?: ProfileUserModel()
            viewModel.isKmSetting = viewModel.profileUser.unit == TypeUnits.METRIC.index

            binding.itemBMR.setDataView(viewModel.getBMR().toDouble(), true)
            binding.itemSaveWeight.setDataView(viewModel.getCalories().toDouble(), true)
            binding.itemHeat.setDataView(90.0, true)
            binding.itemHeight.setDataView(
                viewModel.profileUser.height,
                false
            )
            binding.itemWeight.setDataView(
                viewModel.profileUser.weight,
                false
            )
            binding.itemAge.setDataView(23.0, true)

            context?.let { it1 ->
                binding.viewHomeBMI.init(it1, viewModel.profileUser, viewModel.isKmSetting)
            }
            binding.viewHomeBMI.showTitleView(true)

            val name =
                viewModel.profileUser.firstname + " " + viewModel.profileUser.lastname
            binding.ivProfile.setDataAvatar(false, name, null, viewModel.profileUser.avatar)
        }

        observe(viewModel.isShowHealthIndex) {
            viewModel.isShowHealthIndex.value = null
            binding.listBody.visibility = if (it) View.GONE else View.VISIBLE
            binding.listHealth.visibility = if (it) View.VISIBLE else View.GONE
            if (it) {
                binding.tvBodyIndex.background = null
                binding.tvHealIndex.background =
                    activity?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1,
                            R.drawable.shape_bg_tabitem
                        )
                    }
            } else {
                binding.tvHealIndex.background = null
                binding.tvBodyIndex.background =
                    activity?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1,
                            R.drawable.shape_bg_tabitem
                        )
                    }
            }
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                activity?.setResult(Activity.RESULT_OK, activity?.intent)
                viewModel.getProfile()
            }
        }
}

