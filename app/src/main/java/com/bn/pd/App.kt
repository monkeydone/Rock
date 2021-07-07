package com.bn.pd

import android.app.Application
import com.example.floatingwindowdemo.custom.floatview.FloatWindowHelper

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        FloatWindowHelper.init(this)
    }
}