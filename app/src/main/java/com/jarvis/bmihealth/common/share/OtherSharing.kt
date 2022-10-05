@file:Suppress("DEPRECATION", "unused")

package com.jarvis.bmihealth.common.share

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.utils.CacheUtil
import com.jarvis.bmihealth.common.share.base.BaseSocialSharing
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.io.File
import java.lang.Exception

/**
 * Created by thangnc on 30-09-2022
 */
class OtherSharing(activity: Activity?, shareLayout: View) :
    BaseSocialSharing(activity, shareLayout) {
    fun savePhoto(nameActivity: String) {
        CacheUtil.savePhotoFromView(this.context, shareLayout, nameActivity)
            .subscribe(object : SingleObserver<Uri?> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(uri: Uri) {
                    scanFile(uri)
                    Toast.makeText(context, "Save photo", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {}
            })
    }

    private fun scanFile(uri: Uri) {
        try {
            val scanFileIntent = Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri
            )
            context.sendBroadcast(scanFileIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun share() {
        CacheUtil.saveViewMap(this.context, shareLayout).subscribe(object : SingleObserver<File?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onSuccess(file: File) {
                showDialogShare(file)
            }

            override fun onError(e: Throwable) {}
        })
    }

    private fun showDialogShare(file: File?) {
        try {
            if (file == null || !file.exists()) {
                return
            }
            val uriForFile: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    this.context,
                    this.context.packageName + ".provider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_STREAM, uriForFile)
            intent.putExtra(Intent.EXTRA_TEXT, "HealthyU")
            intent.type = "image/*"
            this.context.startActivity(Intent.createChooser(intent, "Share image via"))
            this.context.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        this.shareLayout = shareLayout
    }
}