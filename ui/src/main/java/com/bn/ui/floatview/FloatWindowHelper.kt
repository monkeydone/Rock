package com.example.floatingwindowdemo.custom.floatview

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.*
import com.bn.utils.toast

object FloatWindowHelper {
    private var windowManager: WindowManager? = null
    private var view: FloatViews? = null
    private var lastX: Int = 0
    private var lastY: Int = 0
    private var downTime = 0L
    private var isDraged = false

    @SuppressLint("ClickableViewAccessibility")
    private fun addView(context: Context) {
        if (windowManager == null) {
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        }
        var layoutParam = WindowManager.LayoutParams()
        //设置宽和高
        layoutParam.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParam.width = WindowManager.LayoutParams.WRAP_CONTENT
        //设置初始位置在左上角
        layoutParam.format = PixelFormat.TRANSPARENT
        layoutParam.gravity = Gravity.START or Gravity.TOP

        var displayMetrics: DisplayMetrics? = context.resources.displayMetrics
        var screenWidth = displayMetrics?.widthPixels ?: 0
        var screenHeight = displayMetrics?.heightPixels ?: 0
//        layoutParam.verticalMargin = 0.2f
        // FLAG_LAYOUT_IN_SCREEN：将window放置在整个屏幕之内,无视其他的装饰(比如状态栏)； FLAG_NOT_TOUCH_MODAL：不阻塞事件传递到后面的窗口
        layoutParam.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        //设置悬浮窗属性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            // 设置窗体显示类型(TYPE_TOAST:与toast一个级别)
            layoutParam.type = WindowManager.LayoutParams.TYPE_TOAST
        }
        if (view == null)
            view = FloatViews(context)
        windowManager?.addView(view, layoutParam)
        view?.needAttach = true
        view?.handleTouchEvent = { v, ev ->
            handleTouchEvent2(layoutParam, v, ev)
        }

    }


    private fun handleTouchEvent2(
        layoutParam: WindowManager.LayoutParams,
        v: View?,
        ev: MotionEvent?
    ): Boolean {
        if (v == null || ev == null)
            return false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.rawX.toInt()
                lastY = ev.rawY.toInt()
                downTime = System.currentTimeMillis()
                isDraged = false
            }
            MotionEvent.ACTION_MOVE -> {
                var dx = ev.rawX.toInt() - lastX
                var dy = ev.rawY.toInt() - lastY
                var l = v.left + dx
                var r = v.right + dx
                var t = v.top + dy
                var b = v.bottom + dy
                //当滑动出边界时需要重新设置位置
                if (l < 0) {
                    l = 0
                    r = v.width
                }
                if (t < 0) {
                    t = 0
                    b = v.height
                }
                v.layout(l, t, r, b)
                lastX = ev.rawX.toInt()
                lastY = ev.rawY.toInt()
                layoutParam.x = lastX - v.width / 2
                layoutParam.y = lastY - v.height / 2
                windowManager?.updateViewLayout(v, layoutParam)
            }
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - downTime < ViewConfiguration.getTapTimeout()) {
                    v.performClick()
                    isDraged = false
                } else {
                    isDraged = true
                }
                val screenWidth = v.context.resources.displayMetrics.widthPixels
//                        LogUtils.instance.getLogPrint(screenHeight.toString())
                if ((v as FloatViews).needAttach && screenWidth != 0) {
                    if (lastX > screenWidth / 2) {
                        layoutParam.x = screenWidth - v.width
                        view?.isLeft = false
                    } else {
                        layoutParam.x = 0
                        view?.isLeft = true
                    }
                    windowManager?.updateViewLayout(v, layoutParam)
                }
            }
        }
        return isDraged
    }
    fun showView(context: Context) {
        if (view == null || view?.windowToken == null) {
            addView(context)
        }
        if (view != null)
            view?.visibility = View.VISIBLE
    }

    fun hideView(context: Context) {
        view?.visibility = View.GONE
    }

    private var activityCount = 0


    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStarted(activity: Activity) {
                if (activityCount == 0) {
                    hideView(application)
                }
                ++activityCount
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
                --activityCount
                if (activityCount == 0) {
                    showView(application)
                }
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityResumed(activity: Activity) {
            }
        })
    }

    fun requestPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
//            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show()
            "当前无权限，请授权".toast()
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
            intent.data = Uri.parse("package:${activity.packageName}")
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }

    fun handleActivityResult(context: Context, requestCode: Int) {
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                "授权失败".toast()
            } else {
                "授权成功".toast()
            }
        }
    }

    private const val REQUEST_CODE = 100
}