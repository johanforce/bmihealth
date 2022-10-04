@file:Suppress("UNUSED_PARAMETER")

package com.jarvis.bmihealth.presentation.profile

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.FragmentProfileBinding
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.jarvis.bmihealth.presentation.home.HomeViewModel
import com.jarvis.bmihealth.presentation.main.MainActivity
import com.jarvis.bmihealth.presentation.pref.AppPreferenceKey
import com.jarvis.bmihealth.presentation.pref.ThemeMode
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import com.jarvis.bmihealth.presentation.selectmode.view.SelectModeActivity
import com.jarvis.bmihealth.presentation.utilx.Constant
import com.jarvis.bmihealth.presentation.utilx.TypeUnit
import com.jarvis.bmihealth.presentation.utilx.click
import com.jarvis.bmihealth.presentation.utilx.observe
import com.jarvis.locale_helper.helper.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@Suppress("unused")
@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun setUpViews() {
        binding.lifecycleOwner = this

        setOnClickView()
        viewModel.getProfile()
    }

    private fun initData() {
        val name =
            viewModel.profileUser.firstname + " " + viewModel.profileUser.lastname
        binding.avatar.setDataAvatar(false, name, null, viewModel.profileUser.avatar)
        binding.tvName.text = name
        binding.tvBio.text = viewModel.profileUser.bio
        binding.tvBio.isVisible = !binding.tvBio.text.isNullOrEmpty()
        binding.tvName.text = name

        changeUnit(viewModel.isKmSetting)
        changeDarkMode()
        changeLanguage()
    }

    private fun changeLanguage() {
        binding.viewSetting.viewLanguage.viewBinder.setTextValueStyle1(
            LocaleHelper.getInstance()
                .getCurrentDisplayLanguage((activity as? MainActivity))
        )
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            viewModel.getProfile()
        }
    }

    override fun observeData() {
        super.observeData()

        observe(viewModel.profileUsers) {
            viewModel.profileUser = it.firstOrNull() ?: ProfileUserModel()
            viewModel.updateDataView()
            initData()
        }

    }

    private fun setOnClickView() {
        binding.tvEditProfile.click {
            val intent = Intent()
            context?.let { it1 -> intent.setClass(it1, RegisterActivity::class.java) }
            intent.putExtra(Constant.NEXT_SCREEN_TO_PROFILE, true)
            resultLauncher.launch(intent)
        }

        binding.viewSetting.viewUnit.click {
            changeUnitProfileUser()
        }

        binding.viewSetting.viewDarkMode.click {
            val intent = Intent(activity, SelectModeActivity::class.java)
            startActivity(intent)
        }

        binding.viewSetting.viewLanguage.click {
            LocaleHelper.getInstance()
                .changeLanguage(
                    activity, (activity as? MainActivity)?.localeDelegate
                ) {
                    appPreference?.put(AppPreferenceKey.KEY_IS_CHANGE_LANGUAGE, true)
                    appPreference?.put(
                        AppPreferenceKey.KEY_LOCALE_SETTING,
                        Locale.getDefault().toString()
                    )
                    (activity as? MainActivity)?.finish()
                    changeLanguage()
                }
        }
    }


    private fun changeUnit(isKmSetting: Boolean) {
        if (isKmSetting) {
            binding.viewSetting.viewUnit.viewBinder.setTextValueStyle1(getString(R.string.setting_unit_metric))
        } else
            binding.viewSetting.viewUnit.viewBinder.setTextValueStyle1(getString(R.string.setting_unit_imperial))
    }

    private fun changeUnitProfileUser() {
        viewModel.isKmSetting = !viewModel.isKmSetting
        viewModel.profileUser.unit =
            if (viewModel.isKmSetting) TypeUnit.METRIC else TypeUnit.IMPERIAL
        viewModel.updateProfile(viewModel.profileUser)
        changeUnit(viewModel.isKmSetting)
    }

    fun changeDarkMode() {
        val themeMode = viewModel.profileUser.themeMode
        var textState = getString(R.string.all_off)
        when (themeMode) {
            ThemeMode.LIGHT -> {
                textState = getString(R.string.all_off)
            }
            ThemeMode.DARK -> {
                textState = getString(R.string.all_on)
            }
            ThemeMode.FOLLOW_SYSTEM -> {
                textState = getString(R.string.all_mode_system)
            }
        }
        binding.viewSetting.viewDarkMode.viewBinder.setTextValueStyle1(textState)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                activity?.setResult(Activity.RESULT_OK, activity?.intent)
                viewModel.getProfile()
            }
        }
}


