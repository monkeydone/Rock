package com.a.base

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class BaseViewModel<T>(application: Application) : AndroidViewModel(application) {
    var canLoadMore: Boolean = true
    open var requestCount: Int = 20
    open var requestCountL: Long = 20L
    var lastIdStr = ""
    var lastIdLong = 0L
    var lastIdInt = 0
    lateinit var intent: Intent

    open var startIndex: Int = -requestCount
    var loadingLive = MutableLiveData<Boolean>()
    var noDataLive = MutableLiveData<Boolean>()
    var noNetwork = MutableLiveData<Int>()
    var noCacheContent = MutableLiveData<Boolean>()
    var itemList: MutableLiveData<T> = MutableLiveData()
    var nextPageLoadingLive = MutableLiveData<Boolean>()
    var canReload = false

    open fun loadData(): LiveData<T> {
        //加载数据的方法
        return itemList
    }

    open fun reloadData(): LiveData<T> {
        canReload = true
        return loadData()
    }

    fun initIntent(intent: Intent) {
        this.intent = intent
    }

    fun postSkeleton(value: Boolean) {
        loadingLive.value = value
    }

    private val loading = MutableLiveData<Boolean>()
    private var loadingQueue = 0
    fun postLoading(value: Boolean) {
        if (value) {
            sendLoading(value)
            loadingQueue++
        } else {
            if (loadingQueue > 0) loadingQueue--
            sendLoading(value)
        }
    }

    private fun sendLoading(value: Boolean) {
        if (loadingQueue == 0) loading.value = value
    }

    fun notifyLoading(lifecycleOwner: LifecycleOwner, callBack: (Boolean) -> Unit) {
        lifecycleOwner.observer(loading, callBack)
    }

}
