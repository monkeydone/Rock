package com.bn.pd

import android.app.Application
import android.view.View
import com.bn.pd.http.ApiClient
import com.bn.utils.toast
import com.example.floatingwindowdemo.custom.floatview.FloatWindowHelper

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        FloatWindowHelper.init(this)
        FloatWindowHelper.viewClickListener = View.OnClickListener {
            "test3".toast()
        }
        ApiClient.init()
    }
}
