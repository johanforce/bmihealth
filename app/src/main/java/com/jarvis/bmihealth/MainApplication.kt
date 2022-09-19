package com.jarvis.bmihealth

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {
    lateinit var gson: Gson

    companion object {
        private var instance: MainApplication? = null
        var isCountDownTime = false

        fun applicationContext(): MainApplication {
            return instance as MainApplication
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        gson = Gson()
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        //do something
    }

    override fun onActivityStarted(p0: Activity) {
        //do something
    }

    override fun onActivityResumed(p0: Activity) {
        //do something
    }

    override fun onActivityPaused(p0: Activity) {
        //do something
    }

    override fun onActivityStopped(p0: Activity) {
        //do something
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        //do something
    }

    override fun onActivityDestroyed(p0: Activity) {
        isCountDownTime = false
    }

}
