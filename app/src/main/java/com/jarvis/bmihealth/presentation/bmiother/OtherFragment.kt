package com.jarvis.bmihealth.presentation.bmiother

import android.os.Bundle
import com.jarvis.bmihealth.databinding.ActivityMainBinding
import com.jarvis.bmihealth.databinding.FragmentBmiotherBinding
import com.jarvis.bmihealth.databinding.FragmentHomeBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment


class OtherFragment :
    BaseFragment<FragmentBmiotherBinding, OtherViewModel>(FragmentBmiotherBinding::inflate) {
    override fun getViewModelClass() = OtherViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.otherViewModel = viewModel
        binding.lifecycleOwner = this


    }


}
