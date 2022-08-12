package com.jarvis.bmihealth.presentation.utilx

import android.util.Log

object LogUtil {
    fun ct(log: String) {
            val stackTrace = Exception().stackTrace[1]
            var fileName = stackTrace.fileName
            if (fileName == null) fileName =
                "" // It is necessary if you want to use proguard obfuscation.
            val info = (stackTrace.methodName + " (" + fileName + ":"
                    + stackTrace.lineNumber + ")")
            Log.i("=CT", "$info: $log")
    }
}
