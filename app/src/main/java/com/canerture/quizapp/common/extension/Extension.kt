package com.canerture.quizapp.common.extension

import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun handler(delay: Long, function: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        function()
    }, delay)
}

fun Resources.toPixel(dp: Int) =
    dp.toFloat() * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

fun Resources.toPixel(dp: Float) =
    dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)