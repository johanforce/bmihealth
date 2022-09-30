package com.jarvis.bmihealth.presentation.bmiother

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.jarvis.bmihealth.databinding.ActicityPreviewBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.home.ViewHomeBMI
import com.jarvis.bmihealth.presentation.utilx.StorageUtils
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.design_system.toolbar.OnToolbarActionListener


class PreviewActivity :
    BaseActivity<ActicityPreviewBinding, ResultViewModel>(ActicityPreviewBinding::inflate),
    OnToolbarActionListener {

    private val viewModel: ResultViewModel by viewModels()

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

    }

    private fun initView() {
//        this.binding.toolbar.setOnToolbarActiontListener(this)
//
//        binding.viewBMI.init(
//            this,
//            viewModel.otherModel.value!!.weight,
//            viewModel.otherModel.value!!.height,
//            viewModel.otherModel.value!!.birthday,
//            viewModel.otherModel.value!!.gender,
//            viewModel.isKmSetting.value!!,
//            null
//        )
//        binding.viewBMI.disableClickView()
//
//        viewModel.loadAvatar(binding.ivGender, this)
//        this.binding.ivHeight.setUnit(viewModel.getUnit(this))
//        this.binding.viewBMI.setOnClick(object : ViewHomeBMI.OnClickListener {
//            override fun onClickHealthy() {
//            }
//
//            override fun onClickBody() {
//            }
//
//            override fun onClickBMI() {
//            }
//
//        });
    }

    override fun onToolbarTextCtaClick() {

    }

    override fun onToolbarAction1Click() {

    }

    override fun onToolbarAction2Click() {
//        if (StorageUtils.checkReadWriteStoragePermission()) {
//            viewModel.saveImage(binding.clPreview, this)
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(
//                    arrayOf(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ), 111
//                )
//            }
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        for (grantResult in grantResults) {
//            if (grantResult == PackageManager.PERMISSION_GRANTED) {
//                viewModel.saveImage(binding.clPreview, this)
//                break
//            }
//        }
    }
}