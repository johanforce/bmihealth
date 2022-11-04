package com.jarvis.bmihealth.presentation.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.jarvis.bmihealth.common.cropimage.PermissionConst
import com.jarvis.bmihealth.common.enums.ActivityEnum
import com.jarvis.bmihealth.common.enums.GoalEnum
import com.jarvis.bmihealth.common.enums.TypeUnits
import com.jarvis.bmihealth.common.extensions.click
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.databinding.ActivityRegisterBinding
import com.jarvis.bmihealth.domain.model.model.OtherProfile
import com.jarvis.bmihealth.domain.model.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.common.Constant
import com.jarvis.bmihealth.presentation.main.MainActivity
import com.jarvis.bmihealth.presentation.register.widget.ViewInputHealthInfo
import com.jarvis.bmihealth.presentation.register.widget.ViewInputProfileInfo
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused", "DEPRECATION")
@AndroidEntryPoint
class RegisterActivity :
    BaseActivity<ActivityRegisterBinding, RegisterViewModel>(ActivityRegisterBinding::inflate) {

    private val viewModel: RegisterViewModel by viewModels()

    private var isGoToFromProfile = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    companion object {
        const val KEY_USER_INFO = "user_info"
    }

    override fun getToolbar(): JxToolbar {
        return binding.tbAddProfile
    }

    private fun getDataIntent() {
        isGoToFromProfile = intent.getBooleanExtra(Constant.NEXT_SCREEN_TO_PROFILE, false)
    }

    override fun setUpViews() {
        super.setUpViews()
        getDataIntent()
        setOnClick()
        binding.viewInfo.setEnableErrorStateInputField(false)
        viewModel.getProfile()
    }

    private fun initDataToOnBoarding() {
        binding.viewInfoOther.initDefaultValue(OtherProfile())
        binding.viewRPE.init(
            this,
            viewModel.profileUser.unit == TypeUnits.METRIC.index,
            ActivityEnum.MODERATELY.index,
            GoalEnum.MAINTAIN_WEIGHT.index
        )
    }

    private fun initDataToProfile() {
        binding.viewInfo.setUserProfile(viewModel.profileUser)
        binding.viewInfoOther.setUserInfo(viewModel.profileUser)
        binding.viewRPE.init(
            this,
            viewModel.profileUser.unit == TypeUnits.METRIC.index,
            viewModel.profileUser.activityLevel ?: ActivityEnum.MODERATELY.index,
            viewModel.profileUser.goal ?: GoalEnum.MAINTAIN_WEIGHT.index
        )
    }

    override fun observeData() {
        super.observeData()
        observe(viewModel.profileUsers) {
            viewModel.profileUser = it.firstOrNull() ?: ProfileUserModel()

            if (it.isEmpty()) {
                initDataToOnBoarding()
            } else {
                initDataToProfile()
            }
        }

        observe(viewModel.isInsertProfile) {
            setResult(Activity.RESULT_OK, intent)
            if (!isGoToFromProfile) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                finish()
            }
        }
    }


    private fun setOnClick() {
        binding.viewInfo.setOnClickListener(object : ViewInputProfileInfo.OnClickListener {
            override fun onChooseAvatar() {
                binding.viewInfo.clearFocusView()
                binding.viewInfo.initDialogChooseImage()
            }
        })

        binding.viewInfoOther.setOnClickListener(object :
            ViewInputHealthInfo.OnClickProfileListener {
            override fun unitProfileUser(isKm: Boolean) {
                //do nothing
            }

            override fun clickGender() {
                binding.viewInfo.clearFocusView()
            }

            override fun clickDialog() {
                binding.viewInfo.clearFocusView()
            }
        })

        binding.layoutNext.btNext.click {
            val userInfo = getDataUserProfileModel()
            if (viewModel.profileUsers.value?.isEmpty() == true) {
                viewModel.insertProfile(userInfo)
            } else {
                updateUserProfileModel()
                viewModel.updateProfile(viewModel.profileUser)
            }
        }
    }

    private fun getDataUserProfileModel(): ProfileUserModel {
        return ProfileUserModel(
            1,
            binding.viewInfo.getFirstName() ?: "",
            binding.viewInfo.getLastName() ?: "",
            binding.viewInfoOther.gender,
            binding.viewInfoOther.birthDay,
            HealthIndexUtils.calculateAgeInYear(binding.viewInfoOther.birthDay),
            binding.viewInfo.byteArray,
            "",
            binding.viewInfoOther.weight,
            binding.viewInfoOther.unit,
            binding.viewInfoOther.height,
            binding.viewInfo.getBio(),
            "VN",
            binding.viewRPE.goalTemp,
            binding.viewRPE.activityTemp
        )
    }

    private fun updateUserProfileModel() {
        viewModel.profileUser.apply {
            firstname = binding.viewInfo.getFirstName() ?: ""
            lastname = binding.viewInfo.getLastName() ?: ""
            gender = binding.viewInfoOther.gender
            birthday = binding.viewInfoOther.birthDay
            age = HealthIndexUtils.calculateAgeInYear(binding.viewInfoOther.birthDay)
            avatar = binding.viewInfo.byteArray
            weight = binding.viewInfoOther.weight
            unit = binding.viewInfoOther.unit
            height = binding.viewInfoOther.height
            bio = binding.viewInfo.getBio()
            goal = binding.viewRPE.goalTemp
            activityLevel = binding.viewRPE.activityTemp
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        binding.viewInfo.executeOnPermissionResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.viewInfo.executeOnActivityResult(requestCode, resultCode, data, contentResolver)
    }

    fun onClickChooseCamera() {
        ActivityCompat.requestPermissions(
            this,
            PermissionConst.REQUIRED_PERMISSIONS_CAMERA,
            PermissionConst.REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

