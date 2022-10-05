package com.jarvis.bmihealth.presentation.onboarding

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import com.jarvis.bmihealth.databinding.FragmentTabOnbroadingBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment
import com.jarvis.bmihealth.presentation.onboarding.model.OnBoardingModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class OnBoardingFragment :
    BaseFragment<FragmentTabOnbroadingBinding>(FragmentTabOnbroadingBinding::inflate) {
    companion object {
        const val DATA_CLASS = "data_class"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = this.arguments ?: return
        val onBoarDingModel = arguments.getString(DATA_CLASS)
        val fromJson: OnBoardingModel =
            GsonBuilder().create().fromJson(onBoarDingModel, OnBoardingModel::class.java)
        updateValue(fromJson)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateValue(onBoarDingModel: OnBoardingModel?) {
        if (onBoarDingModel == null) {
            return
        }
        binding.ivAvatar.setImageDrawable(context?.resources?.getDrawable(onBoarDingModel.drawable!!))
        binding.tvContent.text = onBoarDingModel.string
        binding.tvTitleSplash.text = onBoarDingModel.title
    }
}
