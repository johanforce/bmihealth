package com.jarvis.bmihealth.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.jarvis.bmihealth.databinding.ActivityOnboardingBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.main.MainActivity
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import com.jarvis.bmihealth.presentation.utilx.observe
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused")
@AndroidEntryPoint
class OnBoardingActivity :
    BaseActivity<ActivityOnboardingBinding, OnBoardingViewModel>(ActivityOnboardingBinding::inflate) {

    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initData()
    }

    private fun initData() {
        viewModel.getProfile()
    }

    override fun observeData() {
        observe(viewModel.profileUsers) {
            if (it.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                binding.clView.isVisible = true
            }
        }
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