package com.example.floatingwindowdemo.custom.floatview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bn.ui.R
import com.bvapp.arcmenulibrary.ArcMenu


class FloatViews : LinearLayout, View.OnClickListener {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {}
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr) {
    }

    private var image: AppCompatImageView? = null
    private var text: TextView? = null

    //是否需要依附边缘
    var needAttach = false

    init {
        View.inflate(context, R.layout.float_views_layout, this)
//        image = findViewById(R.id.img_float_window)
//        text = findViewById(R.id.text_float_window)
        initMenuView(this)
        setOnClickListener(this)
        //减去虚拟按键的高度
//        screenHeight -= DeviceUtils.instance.getVirtualBarHeight(context)
    }

    object MenuItem {
        val ITEM_DRAWABLES = intArrayOf(
            R.mipmap.facebook,
            R.mipmap.twitter, R.mipmap.flickr, R.mipmap.instagram,
            R.mipmap.skype, R.mipmap.github
        )
        val STR = arrayOf("Facebook", "Twiiter", "Flickr", "Instagram", "Skype", "Github")
    }

    fun handleDirection(left:Boolean) {
        val menu = findViewById<ArcMenu>(R.id.arcMenu)
        val p = menu.layoutParams
        if(left) {
            menu.setMenuGravity(ArcMenu.BOTTOM_LEFT)
        }else {
            menu.setMenuGravity(ArcMenu.BOTTOM_RIGHT)
        }

    }

    private fun initMenuView(root: ViewGroup){
        val menu = root.findViewById<ArcMenu>(R.id.arcMenu)
//        menu.attachToScrollView(scrollView)
        menu.showTooltip(true)
        menu.setToolTipBackColor(Color.BLUE)
        menu.setToolTipCorner(6f)
        menu.setToolTipPadding(4f)
        menu.setToolTipTextSize(14)
        menu.setToolTipTextColor(Color.WHITE)

        val itemCount: Int = MenuItem.ITEM_DRAWABLES.size
        for (i in 0 until itemCount) {
            val item = ImageView(root.getContext())
            item.setImageResource(MenuItem.ITEM_DRAWABLES.get(i))
            val position = i
            menu.addItem(item, MenuItem.STR.get(i)) { }
        }
    }

    override fun onClick(v: View?) {
//        LogUtils.instance.getLogPrint("点击了可拖动控件" + v?.context?.packageName)
    }
}