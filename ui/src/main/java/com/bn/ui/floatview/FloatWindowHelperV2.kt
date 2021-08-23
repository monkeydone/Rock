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
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bn.utils.toast

object FloatWindowHelperV2 {
    private var windowManager: WindowManager? = null
    private var view: ViewGroup? = null


    @SuppressLint("ClickableViewAccessibility")
    private fun addView(context: Context) {
        if (windowManager == null) {
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        }
        var layoutParam = WindowManager.LayoutParams()
        //设置宽和高
        layoutParam.height = WindowManager.LayoutParams.MATCH_PARENT
        layoutParam.width = WindowManager.LayoutParams.MATCH_PARENT
        //设置初始位置在左上角
        layoutParam.format = PixelFormat.TRANSPARENT
        layoutParam.gravity = Gravity.START or Gravity.TOP


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
        setInitPosition(context, layoutParam, false, 300)
        windowManager?.addView(view, layoutParam)

    }

    private fun setInitPosition(
        context: Context,
        layoutParam: WindowManager.LayoutParams,
        isLeft: Boolean,
        y: Int
    ) {
        var displayMetrics: DisplayMetrics? = context.resources.displayMetrics
        var screenWidth = displayMetrics?.widthPixels ?: 0
        var screenHeight = displayMetrics?.heightPixels ?: 0
        layoutParam.y = screenHeight - y
        if (isLeft) {
            layoutParam.x = 0
        } else {
            layoutParam.x = screenWidth - view!!.width
        }

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


    fun init(application: Application, view: ViewGroup) {
        this.view = view
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
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