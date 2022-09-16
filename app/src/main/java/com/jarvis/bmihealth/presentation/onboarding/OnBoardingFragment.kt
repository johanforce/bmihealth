package com.jarvis.bmihealth.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.jarvis.bmihealth.databinding.FragmentTabOnbroadingBinding
import com.jarvis.bmihealth.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment(context: AppCompatActivity) :
    BaseFragment<FragmentTabOnbroadingBinding>(FragmentTabOnbroadingBinding::inflate) {
    companion object {
        const val DATA_CLASS = "data_class"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

    private fun updateValue(onBoarDingModel: OnBoardingModel?) {
        if (onBoarDingModel == null) {
            return
        }
        binding.ivAvatar.setImageDrawable(context?.resources?.getDrawable(onBoarDingModel.drawable!!))
        binding.tvContent.text = onBoarDingModel.string
        binding.tvTitleSplash.text = onBoarDingModel.title
    }
}