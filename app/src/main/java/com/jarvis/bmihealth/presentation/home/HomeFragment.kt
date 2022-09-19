package com.jarvis.bmihealth.presentation.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.bmihealth.databinding.FragmentHomeBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.jarvis.bmihealth.presentation.utilx.click
import com.jarvis.bmihealth.presentation.utilx.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment(context: AppCompatActivity) :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by context.viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initData()
    }

    private fun initData() {
        viewModel.getProfile()
        binding.btAddProfile.click {

        }

    }

    override fun observeData() {
        observe(viewModel.profileUser) {
            binding.tvData.text = it.toString()
        }
    }
}

