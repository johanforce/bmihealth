@file:Suppress("unused", "FunctionName", "UNUSED_PARAMETER", "DEPRECATION")

package com.jarvis.bmihealth.common.cropimage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.utils.FileUtils
import com.yalantis.ucrop.UCrop
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class UCropImagePresenter(context: AppCompatActivity) :
    BaseContext<AppCompatActivity>(context), CoroutineScope {
    companion object {
        const val REQUEST_GET_SINGLE_FILE = 126
        const val REQUEST_TAKE_PHOTO = 114
    }

    private var job = Job()
    private var maxW = 960
    private var maxH: Int = 1200

    @SuppressLint("NotConstructor")
    fun UCropImagePresenter(activity: AppCompatActivity?, maxW: Int, maxH: Int) {
        this.maxW = maxW
        this.maxH = maxH
    }

    @SuppressLint("NotConstructor")
    fun UCropImagePresenter(activity: AppCompatActivity?) {
        maxW = 960
        this.maxH = 1200
    }

    fun startCrop(uri: Uri) {
        try {
            if (isFileTooLarge(uri)) {
                launch(Dispatchers.Main) {
                    val compressImage: File =
                        Compressor.compressImage(context, FileUtils.getFile(context, uri)!!)
                    startCrop(Uri.fromFile(compressImage))
                    return@launch
                }
            }
            var uCrop = UCrop.of(
                context, uri, Uri.fromFile(
                    File(
                        context.cacheDir,
                        "avatar_custom_" + System.currentTimeMillis() + ".jpg"
                    )
                )
            )
            uCrop.withAspectRatio(1F, 1F)
            uCrop = basisConfig(uCrop)
            uCrop = advancedConfig(uCrop)
            uCrop.start(context)
        } catch (e: java.lang.Exception) {
            Toast.makeText(
                context,
                context.resources.getString(R.string.addweight_content_error),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    private fun isFileTooLarge(uri: Uri): Boolean {
//        File file = FileUtils.getFile(context, uri);
        val documentFile = DocumentFile.fromSingleUri(context, uri) ?: return true
        return documentFile.length() > 18000000
    }

    private fun basisConfig(uCrop: UCrop): UCrop {
        return uCrop.withMaxResultSize(maxW, maxH)
    }

    private fun advancedConfig(uCrop: UCrop): UCrop {
        val options: UCrop.Options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setFreeStyleCropEnabled(true)
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.white_5))
        return uCrop.withOptions(options)
    }

    fun galleryAddPic(context: Context, currentPhotoPath: String?): Uri? {
        if (TextUtils.isEmpty(currentPhotoPath)) return null
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = currentPhotoPath?.let { File(it) }
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
        return contentUri
    }

    @Throws(IOException::class)
    fun createImageFile(activity: Activity): File? {
        val imageFileName = "JPEG_health_avatar_"
        val storageDir =
            activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //        File storageDir = activity.getCacheDir();
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun takePictureIntent(activity: Activity?): String? {
        if (activity == null) {
            return ""
        }
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            try {
                photoFile = createImageFile(activity)
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {
                    val photoURI = FileProvider.getUriForFile(
                        activity,
                        activity.packageName + ".provider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        return if (photoFile == null) "" else photoFile.absolutePath
    }

    @SuppressLint("ObsoleteSdkInt")
    fun chooseFromGallery(activity: Activity?) {
        try {
            if (activity == null) {
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val pickIntent = Intent(Intent.ACTION_PICK)
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                activity.startActivityForResult(pickIntent, REQUEST_GET_SINGLE_FILE)
                return
            }
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"
            val pickIntent = Intent(Intent.ACTION_PICK)
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
            activity.startActivityForResult(chooserIntent, REQUEST_GET_SINGLE_FILE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}
