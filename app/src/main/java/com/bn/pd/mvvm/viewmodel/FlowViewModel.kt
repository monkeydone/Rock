package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.a.base.BaseViewModel
import com.bn.utils.random
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


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
                    delay(100 * it.toLong())
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
            loadingLive.value = false
        }

    }

    fun jobNotCancel(done: (String) -> Unit) {
        loadingLive.value = true
        val startTime = System.currentTimeMillis()
        var text = ""
        val job = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var nextTime = startTime
                var i = 0
                while (i < 50) {
                    if (System.currentTimeMillis() >= nextTime) {
                        text += " i:${i} nextTime: ${nextTime} \n"
                        nextTime += 500
                        i++

                    }
                }
            }

        }

        viewModelScope.launch {
            delay(100)
            job.cancelAndJoin()
            done(text)
            loadingLive.value = false
        }
    }

    fun jobCancelOk(done: (String) -> Unit) {

        loadingLive.value = true
        val startTime = System.currentTimeMillis()
        var text = ""
        val job = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var nextTime = startTime
                var i = 0
                while (isActive) {
                    if (System.currentTimeMillis() >= nextTime) {
                        text += " i:${i} nextTime: ${nextTime} \n"
                        nextTime += 500

                    }
                }
            }

        }

        viewModelScope.launch {
            delay(1000)
            job.cancelAndJoin()
            done(text)
            loadingLive.value = false
        }
    }

    fun jobCancelException(done: (String) -> Unit) {

        loadingLive.value = true
        val startTime = System.currentTimeMillis()
        var text = ""
        val job = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var nextTime = startTime
                var i = 0
                try {
                    while (isActive) {
                        if (System.currentTimeMillis() >= nextTime) {
                            text += " i:${i} nextTime: ${nextTime} \n"
                            nextTime += 500

                        }
                    }
                } catch (e: Exception) {
                    text += "exception: ${e.message}"
                } finally {
                    text += "finally "
                }

            }

        }

        viewModelScope.launch {
            delay(1000)
            job.cancelAndJoin()
            done(text)
            loadingLive.value = false
        }
    }

    fun jobCancelException2(done: (String) -> Unit) {

        loadingLive.value = true
        val startTime = System.currentTimeMillis()
        var text = ""
        val job = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var nextTime = startTime
                var i = 0
                try {
                    while (isActive) {
                        if (System.currentTimeMillis() >= nextTime) {
                            text += " i:${i} nextTime: ${nextTime} \n"
                            nextTime += 500

                        }
                    }
                } catch (e: Exception) {
                    text += "exception: ${e.message}"
                } finally {
                    delay(100)
                    text += "finally "
                }

            }

        }

        viewModelScope.launch {
            delay(1000)
            job.cancelAndJoin()
            done(text)
            loadingLive.value = false
        }
    }

    fun jobNoCancellable(done: (String) -> Unit) {

        loadingLive.value = true
        val startTime = System.currentTimeMillis()
        var text = ""
        val job = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var nextTime = startTime
                var i = 0
                try {
                    while (isActive) {
                        if (System.currentTimeMillis() >= nextTime) {
                            text += " i:${i} nextTime: ${nextTime} \n"
                            nextTime += 500

                        }
                    }
                } catch (e: Exception) {
                    text += "exception: ${e.message}"
                } finally {
                    withContext(NonCancellable) {
                        delay(100)
                        text += "finally "
                    }
                }

            }

        }

        viewModelScope.launch {
            delay(1000)
            job.cancelAndJoin()
            done(text)
            loadingLive.value = false
        }
    }

    fun jobTimeout(done: (String) -> Unit) {

        loadingLive.value = true
        val startTime = System.currentTimeMillis()
        var text = ""
        val job = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    withTimeout(1000) {
                        repeat(10000) {
                            delay(100)
                            text += " i :${it} "
                        }
                    }
                } catch (e: Exception) {
                    text += "timeoutException: ${e.message}"
                }

            }

        }

        viewModelScope.launch {
            delay(5000)
//            job.cancelAndJoin()
            done(text)
            loadingLive.value = false
        }
    }

    fun doSomething(done: (String) -> Unit) {
        loadingLive.value = true
        viewModelScope.launch {
            val o = 14.random()
            val t = 28.random()
            val one = async { doSomethingDelay(100, o) }
            val two = async { doSomethingDelay(2000, t) }

            val result = "${o} + ${t} = ${one.await() + two.await()}"
            done(result)
            loadingLive.value = false
        }

    }


    fun doSomethingMeasureTime(done: (String) -> Unit) {
        var result = ""
        viewModelScope.launch {
            val time = async {
                measureTimeMillis {
                    doSomethingLazy() {
                        result = it
                    }
                    delay(3000)
                }
            }

            done("time: ${time.await()}  result:${result}")
        }

    }

    fun doSomethingLazy(done: (String) -> Unit) {
        loadingLive.value = true
        viewModelScope.launch {

            val o = 14.random()
            val t = 28.random()
            val one = async(start = CoroutineStart.LAZY) { doSomethingDelay(100, o) }
            val two = async(start = CoroutineStart.LAZY) { doSomethingDelay(2000, t) }
            one.start()
            two.start()
            val result = "${o} + ${t} = ${one.await() + two.await()}"
            done(result)
            loadingLive.value = false
        }

    }

    suspend fun doSomethingDelay(delay: Long, result: Int): Int {
        delay(delay)
        return result
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
