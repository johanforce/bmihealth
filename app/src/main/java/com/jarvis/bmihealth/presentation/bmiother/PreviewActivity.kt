package com.jarvis.bmihealth.presentation.bmiother

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.extensions.click
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.common.share.ShareUtil
import com.jarvis.bmihealth.common.utils.StorageUtils
import com.jarvis.bmihealth.databinding.ActicityPreviewBinding
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.design_system.toolbar.OnToolbarActionListener
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import com.well.unitlibrary.UnitConverter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PreviewActivity :
    BaseActivity<ActicityPreviewBinding, PreviewViewModel>(ActicityPreviewBinding::inflate),
    OnToolbarActionListener {

    private val viewModel: PreviewViewModel by viewModels()

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initData()
        setOnClickView()
    }

    private fun initData() {
        viewModel.getProfile()
    }

    private fun setOnClickView() {
        binding.ivShareTwitter.click {
            viewModel.shareViaApp(binding.clPreview, ShareUtil.TWITTER_PACKAGE, this)
        }

        binding.ivShareInstagram.click {
            viewModel.shareViaApp(binding.clPreview, ShareUtil.INSTA_PACKAGE, this)
        }

        binding.ivShareFb.click {
            viewModel.shareViaApp(binding.clPreview, ShareUtil.FACEBOOK_PACKAGE, this)
        }

        binding.ivShareMore.click {
            viewModel.shareOther(binding.clPreview, this)
        }
    }

    override fun observeData() {
        super.observeData()

        observe(viewModel.profileUsers) {
            viewModel.updateDataView()
            initView(viewModel.profileUser, viewModel.isKmSetting)
        }
    }

    private fun initView(otherModel: ProfileUserModel, isKmSetting: Boolean) {
        this.binding.toolbar.setOnToolbarActiontListener(this)

        binding.viewBMI.init(this, viewModel.profileUser, viewModel.isKmSetting)
        binding.viewBMI.disableClickView()

        val name =
            viewModel.profileUser.firstname + " " + viewModel.profileUser.lastname

        binding.ivGender.setDataAvatar(false, name, null, viewModel.profileUser.avatar)

        this.binding.tvName.text = name

        binding.ivMainTain.setIndex(viewModel.getCalories().toString())
        binding.ivBMR.setIndex(viewModel.getBMR().toString())
        this.binding.tvAge.text = getString(
            R.string.all_years_old,
            HealthIndexUtils.calculateAgeInYear(otherModel.birthday)
        )
        if (isKmSetting) {
            this.binding.ivWeight.setIndex(
                UnitConverter.formatDoubleToString(otherModel.weight, 1).toString()
            )
            this.binding.ivWeight.setUnit(getString(R.string.unit_kg))
            this.binding.ivHeight.setIndex(
                UnitConverter.formatDoubleToString(otherModel.height, 0).toString()
            )
            this.binding.ivHeight.setUnit(getString(R.string.unit_cm))
        } else {
            this.binding.ivWeight.setIndex(
                UnitConverter.formatDoubleToString(
                    UnitConverter.convertKgToLbs(
                        otherModel.weight
                    ), 1
                ).toString()
            )
            this.binding.ivWeight.setUnit(getString(R.string.unit_lbs))
            this.binding.ivHeight.setIndex(
                UnitConverter.convertCmToFtStringIfNeed(
                    otherModel.height, false
                ).toString()
            )
            this.binding.ivHeight.setUnit("")
        }
    }

    override fun onToolbarTextCtaClick() {
        TODO("Not yet implemented")
    }

    override fun onToolbarAction1Click() {
        TODO("Not yet implemented")
    }

    override fun onToolbarAction2Click() {
        if (StorageUtils.checkReadWriteStoragePermission()) {
            viewModel.saveImage(binding.clPreview, this)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 111
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                viewModel.saveImage(binding.clPreview, this)
                break
            }
        }
    }
}
