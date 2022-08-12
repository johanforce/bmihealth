package com.jarvis.bmihealth.presentation.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.bmihealth.databinding.FragmentHomeBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.jarvis.bmihealth.presentation.utilx.LogUtil
import com.jarvis.bmihealth.presentation.utilx.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment(context: AppCompatActivity) :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by context.viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        viewModel.getProfile()
        LogUtil.ct("-------vao day--------")
        observe(viewModel.profileUser) {
            binding.tvData.text = it.toString()
        }
    }
}

