package com.jarvis.bmihealth.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.jarvis.bmihealth.databinding.ActivityOnboardingBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.bmiother.OtherViewModel
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

    private val viewModel: OtherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    override fun observeData() {

    }

    override fun setUpViews() {
        binding.btLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.clSkipLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}