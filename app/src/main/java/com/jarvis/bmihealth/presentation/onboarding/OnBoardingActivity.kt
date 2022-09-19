package com.jarvis.bmihealth.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.jarvis.bmihealth.databinding.ActivityOnboardingBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.bmiother.OtherViewModel
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused")
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
}