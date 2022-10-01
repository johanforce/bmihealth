package com.jarvis.bmihealth.presentation.bmiother

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ActivityResultOtherBinding
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import com.jarvis.bmihealth.presentation.utilx.Constant
import com.jarvis.bmihealth.presentation.utilx.click
import com.jarvis.bmihealth.presentation.utilx.observe
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import com.well.unitlibrary.UnitConverter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultOtherActivity :
    BaseActivity<ActivityResultOtherBinding, ResultViewModel>(ActivityResultOtherBinding::inflate) {

    private val viewModel: ResultViewModel by viewModels()

    private val listRequired = arrayListOf(
        R.string.onboarding_strict_loos,
        R.string.onboarding_mormal_weight,
        R.string.onboarding_comfortable,
        R.string.onboarding_maintain,
        R.string.onboarding_normal,
        R.string.onboarding_strict
    )

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        viewModel.getProfile()
        setOnClickView()
    }

    private fun setOnClickView() {
        binding.btShare.setOnClickListener {
            val intent = Intent(this@ResultOtherActivity, PreviewActivity::class.java)
            startActivity(intent)
        }

        binding.btnEdit.click {
            val intent = Intent()
            this.let { it1 -> intent.setClass(it1, RegisterActivity::class.java) }
            intent.putExtra(Constant.NEXT_SCREEN_TO_PROFILE, true)
            resultLauncher.launch(intent)
        }
    }

    override fun observeData() {
        super.observeData()
        observe(viewModel.profileUsers) {
            viewModel.updateDataView()
            initView(viewModel.profileUser, viewModel.isKmSetting)
        }
    }

    private fun initView(profileUser: ProfileUserModel, isKmSetting: Boolean) {
        binding.viewBMI.init(this, profileUser, isKmSetting)
        binding.viewBMI.disableClickView()
        loadDataOther(profileUser, isKmSetting)
    }

    private fun loadDataOther(otherModel: ProfileUserModel, isKmSetting: Boolean) {
        if ((otherModel.goal ?: 0) < 1) {
            otherModel.goal = 1
        } else if ((otherModel.goal ?: 1) > listRequired.size) {
            otherModel.goal = listRequired.size
        }
        this.binding.viewBMI.loadData(
            otherModel,
            isKmSetting
        )

        val name =
            otherModel.firstname + " " + otherModel.lastname
        this.binding.tvName.text = name

        binding.ivMainTain.setTitle(getString(listRequired[(otherModel.goal ?: 1) - 1]))
        binding.ivMainTain.setIndex(viewModel.getCalories().toString())
        binding.ivBMR.setIndex(viewModel.getBMR().toString())
        this.binding.tvAge.text = getString(
            R.string.all_years_old,
            HealthIndexUtils.calculateAgeInYear(otherModel.birthday)
        )
        if (isKmSetting) {
            this.binding.ivWeight.setIndex(
                UnitConverter.formatDoubleToString(otherModel.weight, 1).toString()
            )
            this.binding.ivWeight.setUnit(getString(R.string.unit_kg))
            this.binding.ivHeight.setIndex(
                UnitConverter.formatDoubleToString(otherModel.height, 0).toString()
            )
            this.binding.ivHeight.setUnit(getString(R.string.unit_cm))
        } else {
            this.binding.ivWeight.setIndex(
                UnitConverter.formatDoubleToString(
                    UnitConverter.convertKgToLbs(
                        otherModel.weight
                    ), 1
                ).toString()
            )
            this.binding.ivWeight.setUnit(getString(R.string.unit_lbs))
            this.binding.ivHeight.setIndex(
                UnitConverter.convertCmToFtStringIfNeed(
                    otherModel.height, false
                ).toString()
            )
            this.binding.ivHeight.setUnit("")
        }

        binding.ivGender.setDataAvatar(false, name, null, viewModel.profileUser.avatar)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                this.setResult(Activity.RESULT_OK, intent)
                viewModel.getProfile()
            }
        }
}