package com.jarvis.bmihealth.presentation.bmiother

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.bmihealth.databinding.ActivityMainBinding
import com.jarvis.bmihealth.databinding.FragmentBmiotherBinding
import com.jarvis.bmihealth.databinding.FragmentHomeBinding
import com.jarvis.bmihealth.presentation.base.BaseContext
import com.jarvis.bmihealth.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherFragment(context: AppCompatActivity) :
    BaseFragment<FragmentBmiotherBinding>(FragmentBmiotherBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this


    }
}
