@file:Suppress("unused", "DEPRECATION")

package com.jarvis.bmihealth.presentation.utilx

import android.Manifest
import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.MainApplication
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*

object StorageUtils {
    fun checkReadWriteStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val write =
                ContextCompat.checkSelfPermission(
                    MainApplication.applicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            val read =
                ContextCompat.checkSelfPermission(
                    MainApplication.applicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun convertToBitmap(view: View): Bitmap? {
        val totalHeight = view.height
        val totalWidth = view.width
        val percent = 1f
        val canvasBitmap =
            Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(canvasBitmap)
        canvas.scale(percent, percent)
        view.draw(canvas)
        return canvasBitmap
    }

    fun saveImageToGallery(
        name: String,
        title: String,
        description: String,
        bitmap: Bitmap,
        context: Context
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                writeImageAndroid11(name, bitmap, context)
            } else {
                MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    bitmap,
                    title,
                    description
                )
            }
        } catch (e: IOException) {
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    private fun writeImageAndroid11(dst: String, bitmap: Bitmap, context: Context) {
        Completable.create { emitter: CompletableEmitter ->
            val resolver: ContentResolver =
                context.contentResolver
            val collection: Uri? =
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            val valueDetail = ContentValues()
            valueDetail.put(MediaStore.MediaColumns.DISPLAY_NAME, dst)
            val uri = resolver.insert(collection!!, valueDetail)
            try {
                val fos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                val bitmapdata = fos.toByteArray()
                val bs = ByteArrayInputStream(bitmapdata)


                val outputStream = resolver.openOutputStream(uri!!)
                val bis = BufferedInputStream(bs)
                val bos = BufferedOutputStream(outputStream)
                val buffer = ByteArray(1024)
                var length: Int
                while (bis.read(buffer).also { length = it } > 0) {
                    outputStream!!.write(buffer, 0, length)
                }
                bis.close()
                bos.flush()
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            emitter.onComplete()
        }.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }
}
