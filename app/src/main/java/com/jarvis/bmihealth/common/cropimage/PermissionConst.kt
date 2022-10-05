@file:Suppress("MayBeConstant")

package com.jarvis.bmihealth.common.cropimage

object PermissionConst {
    val REQUEST_CODE_PERMISSIONS = 1001
    val REQUIRED_PERMISSIONS_CAMERA = arrayOf("android.permission.CAMERA")
    val REQUIRED_PERMISSIONS_STORAGE = arrayOf("android.permission.READ_EXTERNAL_STORAGE")
    val REQUEST_CODE_PERMISSIONS_STORAGE = 1002
}
