@file:Suppress("unused")

package com.jarvis.bmihealth.common.share

import android.app.Activity
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.core.content.FileProvider
import com.jarvis.bmihealth.common.utils.CacheUtil.saveViewMap
import com.jarvis.bmihealth.common.utils.DeviceUtil.isPackageExisted
import com.jarvis.bmihealth.common.utils.DeviceUtil.openMarket
import com.jarvis.bmihealth.common.share.base.BaseSocialSharing
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.io.File

/**
 * Created by Minh Lv on 27,March,2019
 */
class PackageNameSharing(context: Activity?, shareLayout: View?, private val packageName: String) :
    BaseSocialSharing(context, shareLayout) {
    override fun share() {
        if (!isPackageExisted(context, packageName)) {
            openMarket(context, packageName)
            return
        }
        saveViewMap(context, shareLayout).subscribe(object : SingleObserver<File> {
            override fun onSubscribe(d: Disposable) {}
            override fun onSuccess(file: File) {
                showDialogShare(file)
            }

            override fun onError(e: Throwable) {}
        })
    }

    private fun showDialogShare(file: File?) {
        try {
            if (file != null && file.exists()) {
                val uriForFile: Uri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider",
                    file
                )
                if (!TextUtils.isEmpty(packageName)) {
                    shareImageByPackage(context, packageName, uriForFile)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}