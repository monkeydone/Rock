package com.bn.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun String.toast() {
    Toast.makeText(ContextUtils.applicationContext, this, Toast.LENGTH_SHORT).show()
}

fun dpToPx(dp: Float): Float {
    return dp * ContextUtils.applicationContext.getResources().getDisplayMetrics().density
}


fun Int.toArtColor() = ContextCompat.getColor(ContextUtils.applicationContext, this)

fun Int.string() = ContextUtils.applicationContext.getString(this)
fun Int.string(vararg args: String) = ContextUtils.applicationContext.getString(this, *args)
fun Int.drawable() = ContextCompat.getDrawable(ContextUtils.applicationContext, this)
fun Int.color() = ContextCompat.getColor(ContextUtils.applicationContext, this)
fun Int.layoutToView(root: ViewGroup? = null): View =
    LayoutInflater.from(ContextUtils.applicationContext).inflate(this, root, false)

fun Int.layoutToDataBinding(viewGroup: ViewGroup? = null): ViewDataBinding =
    DataBindingUtil.inflate(
        LayoutInflater.from(ContextUtils.applicationContext), this, viewGroup, false
    )
