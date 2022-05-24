package com.jarvis.bmihealth.presentation.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.bmihealth.databinding.ActivityMainBinding
import com.jarvis.bmihealth.databinding.FragmentHomeBinding
import com.jarvis.bmihealth.databinding.FragmentProfileBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment(context: AppCompatActivity) :
    BaseFragment<FragmentProfileBinding,>(FragmentProfileBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this


    }
}
