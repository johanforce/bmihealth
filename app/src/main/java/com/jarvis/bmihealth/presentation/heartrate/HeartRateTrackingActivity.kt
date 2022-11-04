@file:Suppress("DEPRECATION")

package com.jarvis.bmihealth.presentation.heartrate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.os.PowerManager
import androidx.activity.viewModels
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.extensions.click
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.databinding.ActivityHeartTrackingBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.common.Constant
import com.jarvis.bmihealth.presentation.heartrate.result.HeartRateResultActivity
import com.jarvis.bmihealth.presentation.heartrate.widget.DialogFingerFound
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeartRateTrackingActivity :
    BaseActivity<ActivityHeartTrackingBinding, HeartRateTrackingViewModel>(
        ActivityHeartTrackingBinding::inflate
    ) {

    private val viewModel: HeartRateTrackingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    override fun setUpViews() {
        super.setUpViews()

        setOnClickView()
        initData()
    }

    private fun initData() {
        initCamera()
        viewModel.getProfile()
    }

    private fun initCamera() {
        viewModel.previewHolder = binding.preview.holder
        viewModel.previewHolder?.addCallback(viewModel.surfaceCallback)

        binding.horizontalProgressBar.progress = 0

        // WakeLock Initialization : Forces the phone to stay On
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        viewModel.wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotDimScreen")
    }

    private fun setOnClickView() {
        binding.toolbar.click {
            this.onBackPressed()
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.wakeLock?.acquire()
        viewModel.camera = Camera.open()
        viewModel.camera?.setDisplayOrientation(90)
    }

    override fun onPause() {
        super.onPause()
        viewModel.wakeLock!!.release()
        viewModel.camera!!.setPreviewCallback(null)
        viewModel.camera!!.stopPreview()
        viewModel.camera!!.release()
        viewModel.camera = null
    }

    override fun observeData() {
        super.observeData()

        observe(viewModel.profileUsers) {
            viewModel.updateDataView()
        }

        observe(viewModel.percentProgress) {
            binding.horizontalProgressBar.progress = it
            if (it == 0) {
                binding.desHeartRate.text = getString(R.string.heart_rate_tutorial_title)
            } else if (it < 85) {
                binding.desHeartRate.text = getString(R.string.heart_rate_checking)
            } else {
                binding.desHeartRate.text = getString(R.string.heart_rate_done)
            }
        }

        observe(viewModel.isTrackingFail) {
            viewModel.isTrackingFail.value = null
            if (it) {
                val dialog = DialogFingerFound()
                dialog.setListener(object : DialogFingerFound.ClickListener {
                    override fun clickStop() {
                        finish()
                    }

                    override fun clickContinue() {
                        dialog.dismiss()
                    }
                })
                dialog.show(supportFragmentManager, "dialog_finger_found")
            }
        }

        observe(viewModel.isTrackingComplete) {
            viewModel.isTrackingComplete.value = null
            if (it) {
                val intent = Intent(this, HeartRateResultActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(Constant.HEART_RATE_RESULT, viewModel.getResultHeartRate())
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
        }

        observe(viewModel.isAddHeartRateSuccess) {
            viewModel.isAddHeartRateSuccess.value = null
            if (it) {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}


