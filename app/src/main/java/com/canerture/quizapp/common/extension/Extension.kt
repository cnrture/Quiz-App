package com.canerture.quizapp.common.extension

import android.content.res.Resources
import android.os.Handler
import android.os.Looper
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

fun Int.toPixel(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()