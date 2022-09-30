@file:Suppress("MemberVisibilityCanBePrivate")

package com.jarvis.bmihealth.presentation.utilx

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.lang.NullPointerException

/**
 * Created by thangnc on 30-09-2019
 */
object CacheUtil {
    fun getUriFromFile(context: Context, file: File?): Uri {
        return FileProvider.getUriForFile(context, context.packageName + ".provider", file!!)
    }

    fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    fun saveViewMap(context: Context, view: View): Single<File> {
        val bitmap = getBitmapFromView(view)
        return getFileFromView(context, bitmap)
    }

    fun saveViewMap(context: Context, bitmap: Bitmap): Single<File> {
        return getFileFromView(context, bitmap)
    }

    @SuppressLint("SetWorldReadable")
    private fun getFileFromView(context: Context, bitmap: Bitmap): Single<File> {
        val observable = Single.create { emitter: SingleEmitter<File> ->
            try {
                val file = File(context.externalCacheDir, "healthy_transfomation.jpg")
                val fOut = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.flush()
                fOut.close()
                file.setReadable(true, false)
                emitter.onSuccess(file)
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
        return observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun savePhotoFromView(context: Context, view: View, nameActivity: String): Single<Uri?> {
        val observable = Single.create { emitter: SingleEmitter<Uri?> ->
            try {
//                    Bitmap bitmap = Bitmap.createScaledBitmap(getBitmapFromView(view), 900, 450, false);
                val bitmap = getBitmapFromView(view)
                val fileName = "healthy_" + nameActivity + "_" + System.currentTimeMillis()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val uri = writeFile(context, bitmap, fileName)
                    if (uri == null) {
                        emitter.onError(NullPointerException("Uri null"))
                    } else {
                        emitter.onSuccess(uri)
                    }
                } else {
                    val file = File(folderFileName, fileName)
                    val fOut = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                    fOut.flush()
                    fOut.close()
                    emitter.onSuccess(Uri.fromFile(file))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
        return observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCacheFolder(context: Context): String {
        val filepath = context.cacheDir.path
        val file = File(filepath, "Healthy")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

    val folderFileName: String
        get() {
            val filepath = Environment.getExternalStorageDirectory().path
            val file = File(filepath, "Healthy")
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath
        }

    private fun cleanDirectory(file: File) {
        try {
            if (!file.exists()) {
                return
            }
            val contentFiles = file.listFiles()
            if (contentFiles != null) {
                for (contentFile in contentFiles) {
                    delete(contentFile)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun delete(file: File) {
        if (!file.isFile || !file.exists()) {
            cleanDirectory(file)
        }
        deleteOrThrow(file)
    }

    @Throws(IOException::class)
    private fun deleteOrThrow(file: File) {
        if (file.exists()) {
            val isDeleted = file.delete()
            if (!isDeleted) {
                throw IOException(String.format("File %s can't be deleted", file.absolutePath))
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun writeFile(context: Context, bitmap: Bitmap, imageName: String): Uri? {
        val resolver = context.applicationContext.contentResolver
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val valueDetail = ContentValues()
        valueDetail.put("_display_name", imageName)
        val uri = resolver.insert(collection, valueDetail)
        try {
            bitmap.compress(
                Bitmap.CompressFormat.PNG, 100, resolver.openOutputStream(
                    uri!!
                )
            )
            return uri
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}