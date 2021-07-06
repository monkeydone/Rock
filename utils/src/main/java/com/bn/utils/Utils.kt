package com.bn.utils

import android.widget.Toast
import androidx.core.content.ContextCompat

fun String.toast() {
    Toast.makeText(ContextUtils.applicationContext, this, Toast.LENGTH_SHORT).show()
}

fun dpToPx(dp: Float): Float {
    return dp * ContextUtils.applicationContext.getResources().getDisplayMetrics().density
}


fun Int.toArtString(): String {
    return ContextUtils.applicationContext.getString(this)
}

fun Int.toArtString(vararg args: Any?) = ContextUtils.applicationContext.getString(this, *args)

fun Int.toArtDrawable() = ContextCompat.getDrawable(ContextUtils.applicationContext, this)

fun Int.toArtColor() = ContextCompat.getColor(ContextUtils.applicationContext, this)

