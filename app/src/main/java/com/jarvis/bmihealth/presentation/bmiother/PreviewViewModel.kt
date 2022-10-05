@file:Suppress("unused")

package com.jarvis.bmihealth.presentation.bmiother

import android.app.Activity
import android.view.View
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.presentation.base.ProfileUserViewModel
import com.jarvis.bmihealth.common.utils.StorageUtils
import com.jarvis.bmihealth.common.share.OtherSharing
import com.jarvis.bmihealth.common.share.PackageNameSharing
import com.jarvis.bmihealth.presentation.bmiother.widget.ViewSaved
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor() : ProfileUserViewModel() {
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
