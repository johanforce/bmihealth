package com.jarvis.bmihealth.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.jarvis.bmihealth.MainApplication
import com.jarvis.bmihealth.databinding.ActivityOnboardingBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.main.MainActivity
import com.jarvis.bmihealth.presentation.pref.AppPreference
import com.jarvis.bmihealth.presentation.pref.AppPreferenceKey.Companion.KEY_IS_INPUT_INFO_SUCCESS
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import com.jarvis.bmihealth.presentation.register.RegisterViewModel
import com.jarvis.bmihealth.presentation.utilx.Gender.Companion.FEMALE
import com.jarvis.bmihealth.presentation.utilx.Gender.Companion.MALE
import com.jarvis.bmihealth.presentation.utilx.OtherProfile
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class OnBoardingActivity :
    BaseActivity<ActivityOnboardingBinding, OnBoardingViewModel>(ActivityOnboardingBinding::inflate) {
    private var otherProfile: OtherProfile? = null
    private var appPreference: AppPreference? = null
    private var isInputSuccess = false

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory)[OnBoardingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as? MainApplication)?.appComponent()?.inject(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
    }

    override fun observeData() {
        viewModel.isMale.observe(this) {
            otherProfile?.gender = if (it) MALE else FEMALE
        }
    }

    override fun setUpViews() {
        this.otherProfile = OtherProfile()
        binding.lifecycleOwner = this
        viewModel.initView()

        this.appPreference = AppPreference.getInstance()
        this.isInputSuccess = appPreference?.get(KEY_IS_INPUT_INFO_SUCCESS, Boolean::class.java)!!
        if (isInputSuccess) {
            openMainActivity()
        }

        viewModel.isMale.observe(this) {
            otherProfile?.gender = if (it) MALE else FEMALE
        }

        binding.btLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.clSkipLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.vIntroduce.onClickListener(object : ViewIntroduceOnBoarding.OnClickListener {
            override fun onClickTermOfService() {

            }

            override fun onClickPrivacyPolicy() {

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun openMainActivity() {
        startActivity(MainActivity::class.java)
        finishAffinity()
    }
}