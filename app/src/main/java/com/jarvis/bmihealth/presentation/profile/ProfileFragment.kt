@file:Suppress("UNUSED_PARAMETER")

package com.jarvis.bmihealth.presentation.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.bmihealth.databinding.FragmentProfileBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.jarvis.bmihealth.presentation.home.HomeViewModel
import com.jarvis.bmihealth.presentation.main.MainActivity
import com.jarvis.bmihealth.presentation.main.MainViewModel
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import com.jarvis.bmihealth.presentation.utilx.click
import com.jarvis.bmihealth.presentation.utilx.observe
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused")
@AndroidEntryPoint
class ProfileFragment(context: AppCompatActivity) :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: HomeViewModel by context.viewModels()
    private val mainViewModel: MainViewModel by context.viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initData()
        setOnClickView()
    }

    private fun setOnClickView() {
        binding.tvEditProfile.click {
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }


    }

    override fun observeData() {
        super.observeData()
        observe(mainViewModel.profileUsers){
            initData()
        }
    }

    private fun initData() {
        val name = mainViewModel.profileUser.firstname + " " + mainViewModel.profileUser.lastname
        binding.avatar.setDataAvatar(false,name,null, mainViewModel.profileUser.avatar)
        binding.tvName.text = name

        binding.viewSetting.viewUnit.viewBinder.setTextValueStyle1()
    }

}

