@file:Suppress("NAME_SHADOWING")

package com.jarvis.widget_custom

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Bundle


object ShapePulseUtil {
    /**
     * The default color for random colors
     */
    private val DEFAULT_RANDOM_COLOUR_SEQUENCE = intArrayOf(
        Color.parseColor("#673AB7"),
        Color.parseColor("#3F51B5"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#03A9F4"),
        Color.parseColor("#00BCD4"),
        Color.parseColor("#009688"),
        Color.parseColor("#8BC34A"),
        Color.parseColor("#4CAF50"),
        Color.parseColor("#FF5722"),
        Color.parseColor("#F44336")
    )

    /**
     * Calculate the current color by the current fraction value.
     *
     * @param fraction The current fraction value
     * @param startValue The start color
     * @param endValue The end color
     * @return The calculate fraction color
     */
    fun evaluateTransitionColor(fraction: Float, startValue: Int, endValue: Int): Int {
        val startA = startValue shr 24 and 0xff
        val startR = startValue shr 16 and 0xff
        val startG = startValue shr 8 and 0xff
        val startB = startValue and 0xff
        val endA = endValue shr 24 and 0xff
        val endR = endValue shr 16 and 0xff
        val endG = endValue shr 8 and 0xff
        val endB = endValue and 0xff
        return startA + (fraction * (endA - startA)).toInt() shl 24 or
                (startR + (fraction * (endR - startR)).toInt() shl 16) or
                (startG + (fraction * (endG - startG)).toInt() shl 8) or
                startB + (fraction * (endB - startB)).toInt()
    }

    /**
     * Generate a list of random colors based on [.DEFAULT_RANDOM_COLOUR_SEQUENCE] colors
     *
     * @return The list of colors
     */
    fun generateRandomColours(): ArrayList<Int> {
        val randomColours = ArrayList<Int>()
        for (color in DEFAULT_RANDOM_COLOUR_SEQUENCE) {
            randomColours.add(color)
        }
        return randomColours
    }
}

internal class LifeCycleManager(private val shapeRipple: ShapeRipple?) :
    ActivityLifecycleCallbacks {
    private var activity: Activity? = null
    fun attachListener() {
        activity = shapeRipple?.context?.let { getActivity(it) }
        activity?.application?.registerActivityLifecycleCallbacks(this)
    }

    private fun detachListener() {
        if (activity == null) {
            return
        }
        activity?.application?.unregisterActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        if (shapeRipple == null || this.activity !== activity) {
            return
        }
        shapeRipple.restartRipple()
    }

    override fun onActivityPaused(activity: Activity) {
        if (shapeRipple == null || this.activity !== activity) {
            return
        }
        shapeRipple.stop()
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        if (this.activity !== activity) {
            return
        }
        detachListener()
    }

    private fun getActivity(context: Context): Activity {
        var context: Context? = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        throw IllegalArgumentException("Context does not derived from any activity, Do not use the Application Context!!")
    }
}