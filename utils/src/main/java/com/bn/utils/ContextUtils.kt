package com.bn.utils

import android.content.Context

object ContextUtils {
    lateinit var applicationContext: Context

    fun init(application: Context) {
        applicationContext = application.applicationContext
    }

    fun isInit(): Boolean {
        return ::applicationContext.isInitialized
    }
}