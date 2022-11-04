package com.jarvis.bmihealth.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class Circle : BaseShape() {
    override fun onSetup(context: Context?, shapePaint: Paint?) {
        //do nothing
    }
    override fun onDraw(
        canvas: Canvas?,
        x: Int,
        y: Int,
        radiusSize: Float,
        color: Int,
        rippleIndex: Int,
        shapePaint: Paint?
    ) {
        shapePaint!!.color = color
        canvas!!.drawCircle(x.toFloat(), y.toFloat(), radiusSize, shapePaint)
    }
}
