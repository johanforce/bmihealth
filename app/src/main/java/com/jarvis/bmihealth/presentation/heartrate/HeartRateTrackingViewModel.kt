@file:Suppress("DEPRECATION", "SpellCheckingInspection")

package com.jarvis.bmihealth.presentation.heartrate

import android.hardware.Camera
import android.os.PowerManager
import android.view.SurfaceHolder
import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.common.enums.AvgRGBEnum
import com.jarvis.bmihealth.common.utils.HealthUtils
import com.jarvis.bmihealth.common.utils.HealthUtils.BPM_MAX_MONITOR
import com.jarvis.bmihealth.common.utils.HealthUtils.BPM_MIN_MONITOR
import com.jarvis.bmihealth.common.utils.HealthUtils.BREATH_MAX_PER_SECOND
import com.jarvis.bmihealth.common.utils.HealthUtils.BREATH_MIN_PER_SECOND
import com.jarvis.bmihealth.common.utils.HealthUtils.MILLISECONDS_PER_SECOND
import com.jarvis.bmihealth.common.utils.HealthUtils.RED_DOTS_PER_FRAME
import com.jarvis.bmihealth.common.utils.HealthUtils.SECOND_PER_MINUTE
import com.jarvis.bmihealth.common.utils.HealthUtils.TIME_COUNT_HEART_RATE
import com.jarvis.bmihealth.common.utils.HealthUtils.TIME_SECONDS_START_MONITOR
import com.jarvis.bmihealth.domain.model.model.HeartRateModel
import com.jarvis.bmihealth.domain.usecase.HeartRateUseCase
import com.jarvis.bmihealth.presentation.base.ProfileUserViewModel
import com.jarvis.heathcarebmi.math.Fft
import com.jarvis.heathcarebmi.math.Fft2
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class HeartRateTrackingViewModel @Inject constructor() : ProfileUserViewModel() {
    @Inject
    lateinit var heartRateUseCase: HeartRateUseCase

    // Variables Initialization
    private val processing = AtomicBoolean(false)

    //bpm variable
    private var beatsPerSecond = 0
    private var valueSPo2 = 0
    private var breathPerSecond = 0
    private var bufferAvgB = 0.0
    private var bufferAvgBreath = 0.0

    var percentProgress = MutableLiveData<Int>()

    //Freq + timer variable
    private var startTime: Long = 0
    private var samplingFreq = 0.0

    //Arraylist
    private var greenAvgList = ArrayList<Double>()
    private var redAvgList = ArrayList<Double>()
    private var blueAvgList = ArrayList<Double>()
    private var counter = 0

    //Sum total intensity dots RGB
    private var sumDotsRed = 0.0
    private var sumDotsBlue = 0.0

    private var standardBlue = 0.0
    private var standardRed = 0.0

    var previewHolder: SurfaceHolder? = null
    var camera: Camera? = null
    var wakeLock: PowerManager.WakeLock? = null

    var isTrackingFail = MutableLiveData<Boolean>()
    var isTrackingComplete = MutableLiveData<Boolean>()
    var isAddHeartRateSuccess = MutableLiveData<Boolean>()

    private val previewCallback =
        Camera.PreviewCallback { data, cam ->
            if (data == null) throw NullPointerException()
            val size = cam.parameters.previewSize ?: throw NullPointerException()

            if (!processing.compareAndSet(false, true)) return@PreviewCallback

            val width = size.width
            val height = size.height

            val greenAvg: Double = ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(
                data.clone(), height, width,
                AvgRGBEnum.GREEN.color
            )
            val redAvg: Double = ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(
                data.clone(),
                height,
                width,
                AvgRGBEnum.RED.color
            )
            val blueAvg: Double = ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(
                data.clone(),
                height,
                width,
                AvgRGBEnum.BLUE.color
            )

            sumDotsRed += redAvg
            sumDotsBlue += blueAvg

            greenAvgList.add(greenAvg)
            redAvgList.add(redAvg)
            blueAvgList.add(blueAvg)

            ++counter

            if (redAvg < RED_DOTS_PER_FRAME) {
                startTime = System.currentTimeMillis()
                percentProgress.value = 0
                counter = 0
                greenAvgList.clear()
                redAvgList.clear()
                blueAvgList.clear()
                sumDotsRed = 0.0
                sumDotsBlue = 0.0
                standardBlue = 0.0
                standardRed = 0.0
                processing.set(false)
            }

            val endTime = System.currentTimeMillis()
            val totalTimeInSecs = (endTime - startTime) / MILLISECONDS_PER_SECOND
            if (totalTimeInSecs >= TIME_SECONDS_START_MONITOR) {
                //mảng các điểm xanh bắt đầu từ thời điểm đo
                val green = greenAvgList.toTypedArray()
                //mảng các điểm đỏ bắt đầu từ thời điểm đo
                val red = redAvgList.toTypedArray()
                //mảng các điểm xanh dương bắt đầu từ thời điểm đo
                val blue = blueAvgList.toTypedArray()
                //Tần suất lấy mẫu
                samplingFreq = counter / totalTimeInSecs
                //Phép đo biến thiên cường độ điểm xanh lá trong 1 Frame
                val hrFreqGreen: Double = Fft.FFT(green, counter, samplingFreq)
                //Cường độ điểm xanh trong 1 phút
                val bpmWithGreen = ceil(hrFreqGreen * SECOND_PER_MINUTE).toInt().toDouble()
                //Phép đo biến thiên cường độ điểm đỏ trong 1 Frame
                val hrFreqRed: Double = Fft.FFT(red, counter, samplingFreq)
                //Cường độ điểm đỏ trong 1 phút
                val bpmWithRed = ceil(hrFreqRed * SECOND_PER_MINUTE).toInt().toDouble()
                //Tần suất hô hấp tính theo cường độ điểm xanh lá
                val rrFrequencyGreen: Double = Fft2.FFT(green, counter, samplingFreq)
                //Nhịp thở tính theo cường độ điểm xanh lá
                val breathWithGreen = ceil(rrFrequencyGreen * SECOND_PER_MINUTE).toInt().toDouble()
                //Tần suất hô hấp tính theo cường độ điểm đỏ
                val rrFrequencyRed: Double = Fft2.FFT(red, counter, samplingFreq)
                //Nhịp thở tính theo cường độ điểm đỏ
                val breathWithRed = ceil(rrFrequencyRed * SECOND_PER_MINUTE).toInt().toDouble()
                //calculating the mean of red and blue intensities on the whole period of recording
                val measumentRedDots: Double = sumDotsRed / counter
                val measumentBlueDots: Double = sumDotsBlue / counter


                //calculating the standard  deviation
                for (i in 0 until counter - 1) {
                    val bufferBlue: Double = blue[i]
                    standardBlue += (bufferBlue - measumentBlueDots) * (bufferBlue - measumentBlueDots)
                    val bufferRed: Double = red[i]
                    standardRed += (bufferRed - measumentRedDots) * (bufferRed - measumentRedDots)
                }

                if (bpmWithGreen > BPM_MIN_MONITOR || bpmWithGreen < BPM_MAX_MONITOR || breathWithGreen > BREATH_MIN_PER_SECOND || breathWithGreen < BREATH_MAX_PER_SECOND) {
                    bufferAvgB = if (bpmWithRed > BPM_MIN_MONITOR || bpmWithRed < BPM_MAX_MONITOR) {
                        (bpmWithGreen + bpmWithRed) / 2
                    } else {
                        bpmWithGreen
                    }

                    bufferAvgBreath =
                        if (bpmWithRed > BPM_MIN_MONITOR || bpmWithRed < BPM_MAX_MONITOR) {
                            (breathWithGreen + breathWithRed) / 2
                        } else {
                            breathWithGreen
                        }
                } else if (bpmWithRed > BPM_MIN_MONITOR || bpmWithRed < BPM_MAX_MONITOR || breathWithRed > BREATH_MIN_PER_SECOND || breathWithRed < BREATH_MAX_PER_SECOND) {
                    bufferAvgB = bpmWithRed
                    bufferAvgBreath = breathWithRed
                }

//                //Nếu BMP < BPM_MIN_MONITOR hoặc > BPM_MAX_MONITOR ngừng đo
//                if (bufferAvgB < BPM_MIN_MONITOR || bufferAvgB > BPM_MAX_MONITOR) {
//                    percentProgress.value = 0
//                    startTime = System.currentTimeMillis()
//                    counter = 0
//                    processing.set(false)
////                    isTrackingFail.value = true
//
//                    return@PreviewCallback
//                }
                beatsPerSecond = bufferAvgB.toInt()
                breathPerSecond = bufferAvgBreath.toInt()
                valueSPo2 = HealthUtils.calculateSPo2(
                    standardRed,
                    standardBlue,
                    measumentRedDots,
                    measumentBlueDots,
                    counter
                )
            }
            if (totalTimeInSecs > TIME_COUNT_HEART_RATE) {
                startTime = System.currentTimeMillis()
                percentProgress.value = 0
                counter = 0
                greenAvgList.clear()
                redAvgList.clear()
                blueAvgList.clear()
                sumDotsRed = 0.0
                sumDotsBlue = 0.0
                standardBlue = 0.0
                standardRed = 0.0
                processing.set(false)
                isTrackingComplete.value = true
            }
            percentProgress.value = (totalTimeInSecs / TIME_COUNT_HEART_RATE * 100).toInt()
            processing.set(false)
        }

    val surfaceCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                camera!!.setPreviewDisplay(previewHolder)
                camera!!.setPreviewCallback(previewCallback)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            val parameters = camera!!.parameters
            parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            val size = getSmallestPreviewSize(width, height, parameters)
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height)
            }
            camera!!.parameters = parameters
            camera!!.startPreview()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            //do nothing
        }
    }

    private fun getSmallestPreviewSize(
        width: Int,
        height: Int,
        parameters: Camera.Parameters
    ): Camera.Size? {
        var result: Camera.Size? = null
        for (size in parameters.supportedPreviewSizes) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size
                } else {
                    val resultArea = result.width * result.height
                    val newArea = size.width * size.height
                    if (newArea < resultArea) result = size
                }
            }
        }
        return result
    }

    fun getResultHeartRate(): HeartRateModel {
        return HeartRateModel(
            heartRate = beatsPerSecond,
            breath = breathPerSecond,
            sPo2 = valueSPo2,
            dateTime = System.currentTimeMillis(),
            id = profileUser.id
        )
    }
}
