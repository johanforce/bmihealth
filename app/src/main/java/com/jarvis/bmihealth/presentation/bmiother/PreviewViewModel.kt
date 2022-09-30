@file:Suppress("unused")

package com.jarvis.bmihealth.presentation.bmiother

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.view.View
import java.lang.Exception
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import com.jarvis.bmihealth.presentation.base.ProfileUserViewModel
import com.jarvis.bmihealth.presentation.utilx.Constant
import com.jarvis.bmihealth.presentation.utilx.StorageUtils
import com.jarvis.bmihealth.presentation.utilx.share.OtherSharing
import com.jarvis.bmihealth.presentation.utilx.share.PackageNameSharing
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import com.well.unitlibrary.UnitConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor() : ProfileUserViewModel() {
    var otherModel = MutableLiveData<ProfileUserModel>()
    var bmrOther = MutableLiveData<Int>()
    var maintainOther = MutableLiveData<Int>()
    var image = MutableLiveData<ByteArray>()

    private val listRequired = arrayListOf(
        R.string.onboarding_strict_loos,
        R.string.onboarding_mormal_weight,
        R.string.onboarding_comfortable,
        R.string.onboarding_maintain,
        R.string.onboarding_normal,
        R.string.onboarding_strict
    )

//    fun getNameRequired(): Int {
//        return listRequired.get(otherModel.value!!.goal - 1)
//    }

    fun shareViaApp(layoutShare: View?, packageName: String, context: Activity) {
        try {
            PackageNameSharing(context, layoutShare, packageName).share()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareOther(view: View, context: Activity) {
        OtherSharing(context, view).share()
    }

    fun loadAvatar(view: AppCompatImageView, context: Activity) {
        if (image.value == null) {
            if (isMale) {
                Glide.with(context).load(R.drawable.others_gender_male_on)
                    .error(R.drawable.ic_avatar_default)
                    .apply(RequestOptions.circleCropTransform())
                    .into(view)
            } else {
                Glide.with(context).load(R.drawable.others_gender_female_on)
                    .error(R.drawable.ic_avatar_default)
                    .apply(RequestOptions.circleCropTransform())
                    .into(view)
            }
        } else {
            Glide.with(context).load(image.value)
                .error(R.drawable.ic_avatar_default)
                .apply(RequestOptions.circleCropTransform())
                .into(view)
        }
    }

    fun saveImage(view: View, context: Activity) {
        val bitmap = StorageUtils.convertToBitmap(view)!!
        StorageUtils.saveImageToGallery(
            "healthy_share_" + System.currentTimeMillis() + ".jpg",
            "healthy_share_" + System.currentTimeMillis(),
            "healthy",
            bitmap,
            context
        )
        val viewSave = ViewSaved(context, bitmap)

        viewSave.show()
    }
}
