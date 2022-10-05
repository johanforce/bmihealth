package com.jarvis.bmihealth.common.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment

object FragmentUtils {
    fun showDialogFragment(
        activity: AppCompatActivity,
        fragment: AppCompatDialogFragment?,
        tag: String?
    ) {
        if (fragment == null) {
            return
        }
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (fragment.isAdded && fragmentManager.findFragmentByTag(tag) != null) {
            if (!fragmentManager.findFragmentByTag(tag)!!.isVisible) {
                fragmentTransaction.show(fragment)
            }
        } else {
            fragment.show(fragmentManager, tag)
        }
    }
}
