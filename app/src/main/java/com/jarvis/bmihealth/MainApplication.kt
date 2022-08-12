package com.jarvis.bmihealth

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.google.gson.Gson
import com.jarvis.bmihealth.di.AppComponent
import com.jarvis.bmihealth.di.DaggerAppComponent
import com.jarvis.bmihealth.di.module.DaoModule
import dagger.hilt.android.HiltAndroidApp

@Suppress("unused")
@HiltAndroidApp
class MainApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {
    lateinit var gson: Gson
    private lateinit var appComponent: AppComponent

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

    private fun initDI() {
        appComponent = DaggerAppComponent.builder()
            .daoModule(DaoModule())
            .build()
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
