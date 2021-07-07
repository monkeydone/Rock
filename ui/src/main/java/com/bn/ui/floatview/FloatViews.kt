package com.example.floatingwindowdemo.custom.floatview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.bn.ui.R
import com.bn.ui.databinding.FloatViewsLayoutBinding


class FloatViews : LinearLayout, View.OnClickListener {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {}
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr) {
    }

    var binding: FloatViewsLayoutBinding
    lateinit var handleTouchEvent: (view: View, event: MotionEvent?) -> Unit

    //是否需要依附边缘
    var needAttach = false
    var isLeft = true

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.float_views_layout,
            this,
            true
        )
        binding.onClickListener = this
        handleViews(isLeft, false)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (::handleTouchEvent.isInitialized) {
            handleTouchEvent(this, ev)
        }

        return super.dispatchTouchEvent(ev)
    }

    fun goTargetView(src: View, dest: View) {
        src.animate().x(dest.x).y(dest.y).setDuration(1000).start()
    }

    fun handleViews(isLeft: Boolean, isShowAll: Boolean) {
        if (isShowAll) {
            binding.imageBottomLeft.visibility = View.VISIBLE
            binding.imageBottomRight.visibility = View.VISIBLE
            binding.imageTopLeft.visibility = View.VISIBLE
            binding.imageTopRight.visibility = View.VISIBLE
            binding.imageCenterLeft.visibility = View.VISIBLE
            binding.imageCenterRight.visibility = View.VISIBLE
        } else {
            binding.imageBottomLeft.visibility = View.GONE
            binding.imageBottomRight.visibility = View.GONE
            binding.imageTopLeft.visibility = View.GONE
            binding.imageTopRight.visibility = View.GONE
            if (isLeft) {
                binding.imageCenterRight.visibility = View.GONE
                binding.imageCenterLeft.visibility = View.VISIBLE
            } else {
                binding.imageCenterLeft.visibility = View.GONE
                binding.imageCenterRight.visibility = View.VISIBLE
            }

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.image_center_left -> {
                handleViews(isLeft, binding.imageBottomLeft.visibility == View.GONE)
            }
            R.id.image_center_right -> {
                handleViews(isLeft, binding.imageBottomLeft.visibility == View.GONE)
            }
        }

    }
}