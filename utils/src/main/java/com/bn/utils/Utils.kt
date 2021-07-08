package com.bn.utils

import android.graphics.Outline
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import java.util.*

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

fun Int.random(): Int {
    val r = Random()
    return r.nextInt(this)
}

fun Int.layoutToDataBinding(viewGroup: ViewGroup? = null): ViewDataBinding =
    DataBindingUtil.inflate(
        LayoutInflater.from(ContextUtils.applicationContext), this, viewGroup, false
    )

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun View.setRoundRect(radius: Float) {
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
    this.clipToOutline = true
}

@BindingAdapter("loadImageByUrl")
fun ImageView.loadImageByUrl(url: String?) {
    Glide.with(this).load(url).into(this)
}