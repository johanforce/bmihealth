package com.jarvis.bmihealth.presentation.bmiother

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.jarvis.bmihealth.databinding.FragmentBmiotherBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused")
@AndroidEntryPoint
class OtherFragment :
    BaseFragment<FragmentBmiotherBinding>(FragmentBmiotherBinding::inflate) {

    private val viewModel: OtherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this


    }
}
