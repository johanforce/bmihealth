package com.jarvis.bmihealth.presentation.profile

import android.os.Bundle
import com.jarvis.bmihealth.databinding.ActivityMainBinding
import com.jarvis.bmihealth.databinding.FragmentHomeBinding
import com.jarvis.bmihealth.databinding.FragmentProfileBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment


class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(FragmentProfileBinding::inflate) {
    override fun getViewModelClass() = ProfileViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.profileViewModel = viewModel
        binding.lifecycleOwner = this


    }


}
