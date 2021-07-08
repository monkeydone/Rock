package com.example.floatingwindowdemo.custom.floatview

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.bn.ui.R
import com.bn.ui.databinding.FloatViewsLayoutBinding
import com.bn.utils.setRoundRect
import com.bn.utils.toast


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
        binding.imageTopLeft.setRoundRect(180.0f)
        binding.imageTopLeft.setBackgroundColor(Color.BLUE)
        binding.onClickListener = this
//        handleViews(isLeft, false)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (::handleTouchEvent.isInitialized) {
            handleTouchEvent(this, ev)
        }

        return super.dispatchTouchEvent(ev)
    }

    fun oneAnimator(list: ArrayList<View>, pos: Int, visibility: Int) {
        val view = list[pos]
        val alpha = if (visibility == View.VISIBLE) 1.0f else 0.0f
        view.animate().alpha(alpha).setDuration(1000)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    val nexPos = pos + 1
                    if (nexPos < list.size) {
                        oneAnimator(list, pos + 1, visibility)
                    }
                    view.visibility = visibility
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            }).start()
    }

    fun handleViewVisible() {
        val list = ArrayList<View>()
        list.add(binding.imageTopLeft)
        list.add(binding.imageTopRight)
        if (isLeft) {
            list.add(binding.imageCenterLeft)
        } else {
            list.add(binding.imageCenterRight)
        }
        list.add(binding.imageBottomRight)
        list.add(binding.imageBottomLeft)

        oneAnimator(list, 0, View.VISIBLE)
    }

    fun handleViewGone() {
        val list = ArrayList<View>()
        list.add(binding.imageTopLeft)
        list.add(binding.imageTopRight)
        if (isLeft) {
            list.add(binding.imageCenterLeft)
        } else {
            list.add(binding.imageCenterRight)
        }
        list.add(binding.imageBottomRight)
        list.add(binding.imageBottomLeft)

        oneAnimator(list, 0, View.INVISIBLE)
    }


    fun handleViews(isLeft: Boolean, isShowAll: Boolean) {
        if (isShowAll) {
            handleViewVisible()
        } else {
            handleViewGone()
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
            R.id.image_top_left -> {
                "test".toast()
            }
        }

    }
}