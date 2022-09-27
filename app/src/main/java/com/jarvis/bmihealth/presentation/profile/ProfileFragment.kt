@file:Suppress("UNUSED_PARAMETER")

package com.jarvis.bmihealth.presentation.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.FragmentProfileBinding
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.jarvis.bmihealth.presentation.home.HomeViewModel
import com.jarvis.bmihealth.presentation.main.MainViewModel
import com.jarvis.bmihealth.presentation.pref.ThemeMode
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import com.jarvis.bmihealth.presentation.selectmode.view.SelectModeActivity
import com.jarvis.bmihealth.presentation.utilx.TypeUnit
import com.jarvis.bmihealth.presentation.utilx.click
import com.jarvis.bmihealth.presentation.utilx.observe
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused")
@AndroidEntryPoint
class ProfileFragment(context: AppCompatActivity) :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: HomeViewModel by context.viewModels()
    private val mainViewModel: MainViewModel by context.viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        setOnClickView()
        mainViewModel.getProfile()
    }

    private fun initData() {
        val name =
            mainViewModel.profileUser.firstname + " " + mainViewModel.profileUser.lastname
        binding.avatar.setDataAvatar(false, name, null, mainViewModel.profileUser.avatar)
        binding.tvName.text = name
        binding.tvBio.text = mainViewModel.profileUser.bio
        binding.tvName.text = name
        changeUnit(mainViewModel.isKmSetting)
        changeDarkMode()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            mainViewModel.getProfile()
        }
    }

    override fun observeData() {
        super.observeData()

        observe(mainViewModel.profileUsers) {
            mainViewModel.profileUser = it.firstOrNull() ?: ProfileUserModel()
            mainViewModel.updateDataView()
            initData()
        }

    }

    private fun setOnClickView() {
        binding.tvEditProfile.click {
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.viewSetting.viewUnit.click {
            changeUnitProfileUser()
        }

        binding.viewSetting.viewDarkMode.click {
            val intent = Intent(activity, SelectModeActivity::class.java)
            startActivity(intent)
        }
    }


    private fun changeUnit(isKmSetting: Boolean) {
        if (isKmSetting) {
            binding.viewSetting.viewUnit.viewBinder.setTextValueStyle1(getString(R.string.setting_unit_metric))
        } else
            binding.viewSetting.viewUnit.viewBinder.setTextValueStyle1(getString(R.string.setting_unit_imperial))
    }

    private fun changeUnitProfileUser() {
        mainViewModel.isKmSetting = !mainViewModel.isKmSetting
        mainViewModel.profileUser.unit =
            if (mainViewModel.isKmSetting) TypeUnit.METRIC else TypeUnit.IMPERIAL
        mainViewModel.updateProfile(mainViewModel.profileUser)
        changeUnit(mainViewModel.isKmSetting)
    }

    fun changeDarkMode() {
        val themeMode = mainViewModel.profileUser.themeMode
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
}


