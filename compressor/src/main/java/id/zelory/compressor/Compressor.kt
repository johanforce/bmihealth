package id.zelory.compressor

import android.content.Context
import android.graphics.Bitmap
import id.zelory.compressor.constraint.*
import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

/**
 * Created on : January 22, 2020
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
object Compressor {
    private fun compress(
            context: Context,
            imageFile: File,
            compressionPatch: Compression.() -> Unit = { default() }
    ): File {
        val compression = Compression().apply(compressionPatch)
        var result = copyToCache(context, imageFile)
        compression.constraints.forEach { constraint ->
            while (constraint.isSatisfied(result).not()) {
                result = constraint.satisfy(result)
            }
        }

        return result
    }


    fun compressImage(context: Context, imageFile: File): File {
        return compress(context, imageFile)
    }

    fun compressImageCustom(context: Context, imageFile: File,
                            width: Int = 1280, height: Int = 720, quality: Int = 80,
                            format: Bitmap.CompressFormat = Bitmap.CompressFormat.WEBP, maxFileSize: Long = 2_097_152): File {

        return compress(context, imageFile) {
            resolution(width, height)
            quality(quality)
            format(format)
            size(maxFileSize) // 2 MB
        }
    }

    fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }
}