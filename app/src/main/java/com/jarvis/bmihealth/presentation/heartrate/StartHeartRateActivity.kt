package com.jarvis.bmihealth.presentation.heartrate

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.databinding.ActivityHeartRateBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused", "DEPRECATION")
@AndroidEntryPoint
class StartHeartRateActivity :
    BaseActivity<ActivityHeartRateBinding, HeartRateViewModel>(ActivityHeartRateBinding::inflate) {

    private val viewModel: HeartRateViewModel by viewModels()

    private var isGoToFromProfile = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    companion object {
        const val KEY_USER_INFO = "user_info"
    }

    override fun setUpViews() {
        super.setUpViews()

        setOnClickView()
    }

    private fun setOnClickView() {

    }


    override fun observeData() {
        super.observeData()

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
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
        }
    }
}

