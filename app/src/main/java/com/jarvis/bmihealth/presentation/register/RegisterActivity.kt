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
import com.jarvis.bmihealth.presentation.pref.AppPreferenceKey
import com.jarvis.bmihealth.presentation.utilx.OtherProfile
import com.jarvis.bmihealth.presentation.utilx.TypeUnit.Companion.METRIC
import com.jarvis.bmihealth.presentation.utilx.cropimage.PermissionConst
import com.jarvis.design_system.toolbar.JxToolbar
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused", "DEPRECATION")
@AndroidEntryPoint
class RegisterActivity :
    BaseActivity<ActivityRegisterBinding, RegisterViewModel>(ActivityRegisterBinding::inflate) {

    private var userInfo: ProfileUserModel? = null
    private var appPreference: AppPreference? = null
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    companion object {
        const val KEY_USER_INFO = "user_info"
    }

    private fun getDataIntent() {

    }

    override fun setUpViews() {
        super.setUpViews()
        getDataIntent()
        setOnClick()
        binding.viewInfo.setEnableErrorStateInputField(false)
        if (userInfo == null) {
            binding.viewInfoOther.initDefaultValue(OtherProfile())
        } else {
            binding.viewInfo.setUserProfile(userInfo)
            binding.viewInfoOther.setUserInfo(userInfo)
        }
        viewModel.getProfile()
        binding.viewRPE.init(this, userInfo?.unit == METRIC, 3, 4)
    }

    override fun getToolbar(): JxToolbar {
        return binding.tbAddProfile
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

        binding.layoutNext.btNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val userInfo = getDataUserProfileModel()
            if(viewModel.profileUsers.value?.isEmpty() == true){
                viewModel.insertProfile(userInfo)
            }
            startActivity(intent)
        }
    }

    private fun getDataUserProfileModel():ProfileUserModel{
        return ProfileUserModel(
            binding.viewInfo.getFirstName() ?: "",
            binding.viewInfo.getLastName() ?: "",
            binding.viewInfoOther.gender,
            binding.viewInfoOther.birthDay,
            23,
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
