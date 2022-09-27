package com.jarvis.bmihealth.presentation.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.jarvis.bmihealth.databinding.ActivityRegisterBinding
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.main.MainActivity
import com.jarvis.bmihealth.presentation.pref.AppPreference
import com.jarvis.bmihealth.presentation.utilx.OtherProfile
import com.jarvis.bmihealth.presentation.utilx.TypeUnit.Companion.METRIC
import com.jarvis.bmihealth.presentation.utilx.click
import com.jarvis.bmihealth.presentation.utilx.cropimage.PermissionConst
import com.jarvis.bmihealth.presentation.utilx.observe
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused", "DEPRECATION")
@AndroidEntryPoint
class RegisterActivity :
    BaseActivity<ActivityRegisterBinding, RegisterViewModel>(ActivityRegisterBinding::inflate) {

    private var appPreference: AppPreference? = null
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
        binding.viewRPE.init(this, viewModel.profileUser.unit == METRIC, 3, 4)
    }

    private fun initDataToProfile() {
        binding.viewInfo.setUserProfile(viewModel.profileUser)
        binding.viewInfoOther.setUserInfo(viewModel.profileUser)
        binding.viewRPE.init(
            this,
            viewModel.profileUser.unit == METRIC,
            viewModel.profileUser.goal ?: 0,
            viewModel.profileUser.activityLevel ?: 4
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
            if (!isGoToFromProfile) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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


}
