package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.a.base.BaseViewModel
import kotlinx.coroutines.*


class FlowViewModel(application: Application) :
    BaseViewModel<List<FlowViewModel.FlowDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class FlowDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L


    fun simpleCoroutines(done: () -> Unit) {
        viewModelScope.launch {
            delay(1000)
            done()
        }
    }

    fun simpleCoroutines2(delay: Long, done: () -> Unit) {
        viewModelScope.launch {
            loadingLive.value = true
            delay(delay)
            simpleCorutinesTimeout(delay, done)
        }
    }

    fun simpleJob1(delay: Long, done: () -> Unit) {
        val job = viewModelScope.launch {
            loadingLive.value = true
            delay(delay)
            done()
            loadingLive.value = false

        }
    }


    fun simpleJob2(delay: Long, done: () -> Unit) {
        val job = viewModelScope.launch {
            loadingLive.value = true
            delay(delay)
            done()
            loadingLive.value = false

        }

        viewModelScope.launch {
            job.join()
        }
    }

    fun simpleJob3(done: (String) -> Unit) {
        loadingLive.value = true
        var text = ""
        viewModelScope.launch {
            delay(1000)
            text += "delay 1000"
        }
        viewModelScope.launch {
            delay(500)
            text += "delay 500"
        }
        viewModelScope.launch {
            delay(3000)
            done(text)
            loadingLive.value = false
        }

    }

    fun simpleJobRepeat(done: (String) -> Unit) {
        loadingLive.value = true
        var text = ""
        for (i in 0..1000) {
            viewModelScope.launch {
                delay(10)
                text += "i:${i}"
            }
        }
        viewModelScope.launch {
            delay(2000)
            done(text)
            loadingLive.value = false
        }

    }

    fun jobCancel(done: (String) -> Unit) {
        loadingLive.value = true
        var text = ""

        val job = viewModelScope.launch {
            repeat(1000) {
                viewModelScope.launch {
                    delay(100)
                    text += "i:${it} "
                }
            }
        }

        viewModelScope.launch {
            delay(1000)
            job.cancel()
            text += "Cancel"
            job.join()
            text += "Done"
            done(text)
//            loadingLive.value = false
        }

    }

    suspend fun simpleCorutinesTimeout(delayMs: Long, done: () -> Unit) {
        withContext(Dispatchers.IO) {
            withTimeout(delayMs) {
                withContext(Dispatchers.Main) {
                    loadingLive.value = false
                    done()
                }
            }
        }

    }

}
