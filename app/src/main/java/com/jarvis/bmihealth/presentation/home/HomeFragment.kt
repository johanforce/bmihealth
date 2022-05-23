package com.jarvis.bmihealth.presentation.home

import android.os.Bundle
import com.jarvis.bmihealth.databinding.ActivityMainBinding
import com.jarvis.bmihealth.databinding.FragmentHomeBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment


class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    override fun getViewModelClass() = HomeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this


    }


}
