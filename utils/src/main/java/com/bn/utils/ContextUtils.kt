package com.bn.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.lang.ref.WeakReference

object ContextUtils {
    lateinit var applicationContext: Context
    private var sCurrentActivityWeakRef: WeakReference<Activity>? = null

    fun init(application: Context) {
        applicationContext = application.applicationContext
        if (applicationContext is Application) {
            initLifecycle(applicationContext as Application)
        }
    }


    fun getTopActivity(): Activity? {
        return sCurrentActivityWeakRef?.get()
    }


    private fun initLifecycle(application: Application) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                sCurrentActivityWeakRef = null
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityResumed(activity: Activity) {
                sCurrentActivityWeakRef = WeakReference<Activity>(activity)
            }
        })
    }

    fun isInit(): Boolean {
        return ::applicationContext.isInitialized
    }
}