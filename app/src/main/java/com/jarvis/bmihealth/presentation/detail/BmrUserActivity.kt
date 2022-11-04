package com.jarvis.bmihealth.presentation.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.databinding.ActivityBmrDetailBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.common.Constant
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.heathcarebmi.utils.UnitConverter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BmrUserActivity :
    BaseActivity<ActivityBmrDetailBinding, DetailViewModel>(ActivityBmrDetailBinding::inflate),
    View.OnClickListener {

    private val viewModel: DetailViewModel by viewModels()

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        initView()
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, intent)
            }
        }

    private fun initView() {
        setOnClickView()
        viewModel.getProfile()
    }

    override fun observeData() = with(viewModel) {
        super.observeData()

        observe(viewModel.profileUsers) {
            viewModel.updateDataView()
            updateView()
        }
    }

    private fun updateView() {
        val bmrUser = viewModel.getBmrUser()
        binding.tvIndexBMR.text = UnitConverter.convertDecimalToString(bmrUser)
    }

    private fun setOnClickView() {
        this.binding.tvEdit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvEdit -> {
                val intent = Intent()
                intent.setClass(this, RegisterActivity::class.java)
                intent.putExtra(Constant.NEXT_SCREEN_TO_PROFILE, true)
                resultLauncher.launch(intent)
            }
        }
    }

}
