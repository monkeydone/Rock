package com.bn.utils

import android.content.Context

object ContextUtils {
    lateinit var applicationContext: Context

    fun init(application: Context) {
        applicationContext = application.applicationContext
    }
}