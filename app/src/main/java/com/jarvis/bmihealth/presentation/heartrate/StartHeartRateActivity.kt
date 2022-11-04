package com.jarvis.bmihealth.presentation.heartrate

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.extensions.click
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.common.utils.TimeUtil
import com.jarvis.bmihealth.databinding.ActivityHeartRateBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.heartrate.diary.DiaryHeartRateActivity
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.design_system.toolbar.OnToolbarActionListener
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused", "DEPRECATION")
@AndroidEntryPoint
class StartHeartRateActivity :
    BaseActivity<ActivityHeartRateBinding, HeartRateViewModel>(ActivityHeartRateBinding::inflate),
    OnToolbarActionListener {

    private val viewModel: HeartRateViewModel by viewModels()

    private var isGoToFromProfile = false

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    companion object {
        const val KEY_USER_INFO = "user_info"
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setOnToolbarActiontListener(this)
        setOnClickView()
        initData()
    }

    private fun initData() {
        binding.tvLastResult.text = getString(R.string.heart_rate_result_null)
        viewModel.getLastResultHeartRate()
    }

    private fun setOnClickView() {
        binding.btStart.click {
            if (!isCheckPermission()) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
            } else {
                val intent = Intent(this, HeartRateTrackingActivity::class.java)
                resultLauncher.launch(intent)
            }
        }
    }


    override fun observeData() {
        super.observeData()

        observe(viewModel.heartRateModel) {
            if (it == null) {
                binding.tvLastResult.text = getString(R.string.heart_rate_result_null)
            } else {
                binding.tvLastResult.text = Html.fromHtml(
                    getString(
                        R.string.heart_rate_result,
                        it.heartRate,
                        TimeUtil.formatDateHeartRate(it.dateTime)
                    )
                )
            }
        }
    }


    private fun isCheckPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    //Camera Permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED -> {
                    val intent = Intent(this, HeartRateTrackingActivity::class.java)
                    resultLauncher.launch(intent)
                }
            }
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, intent)
                viewModel.getProfile()
                viewModel.getLastResultHeartRate()
            }
        }

    override fun onToolbarTextCtaClick() {
        TODO("Not yet implemented")
    }

    override fun onToolbarAction1Click() {
        TODO("Not yet implemented")
    }

    override fun onToolbarAction2Click() {
        val intent = Intent(this, DiaryHeartRateActivity::class.java)
        startActivity(intent)
    }
}

