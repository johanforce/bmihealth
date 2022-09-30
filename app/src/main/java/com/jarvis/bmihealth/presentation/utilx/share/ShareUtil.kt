@file:Suppress("unused")

package com.jarvis.bmihealth.presentation.utilx.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.jarvis.bmihealth.R

object ShareUtil {
    const val INSTA_PACKAGE = "com.instagram.android"
    const val TWITTER_PACKAGE = "com.twitter.android"
    const val FACEBOOK_PACKAGE = "com.facebook.katana"

    fun copyText(context: Context, text: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.share_copy_message), Toast.LENGTH_SHORT)
            .show()
    }
}