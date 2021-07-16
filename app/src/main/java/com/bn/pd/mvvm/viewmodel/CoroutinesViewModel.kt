package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.a.base.BaseViewModel
import com.bn.pd.http.getNetworkService
import com.bn.utils.random
import com.bn.utils.toast
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request


class CoroutinesViewModel(application: Application) :
    BaseViewModel<List<CoroutinesViewModel.CoroutinesDataModel>>(application) {
    var dataLive = MutableLiveData<String>()
    var messageShowLive = MutableLiveData<Boolean>()
    var messageLive = MutableLiveData<String>()

    data class CoroutinesDataModel(val titleResId: Int, val fragment: Fragment)

    var tapCount = 0L

    fun onMainViewClicked() {
        launchDataLoad {
            messageLive.value = getTitle()
            try {
//                getWeather()
                getWeatherWithTimeout2()
//                getWeatherWithTimeoutNotIO()
            } catch (e: Throwable) {
                e.printStackTrace()
                "error".toast()
            }
//            getTitle()
        }
    }


    private val FAKE_RESULTS = listOf(
        "Hello, coroutines!",
        "My favorite feature",
        "Async made easy",
        "Coroutines by example",
        "Check out the Advanced Coroutines codelab next!"
    )

    fun dalay1000() {
        viewModelScope.launch {
            delay(1_000)
            messageLive.value = "${++tapCount} taps at delay 1000"
        }
    }

    fun method1() {
        loadingLive.value = true
        viewModelScope.launch {
            getWeather()
        }
    }

    fun method2() {
        loadingLive.value = true
        viewModelScope.launch {
            getWeatherWithTimeout()
        }
    }

    fun method3() {
        viewModelScope.launch {
            loadingLive.value = true
            delay(3000)
            getWeatherWithTimeout2()
        }
    }

    fun method4() {
        loadingLive.value = true
        viewModelScope.launch {
            fetchDocs()
        }
    }

    fun method5() {
        viewModelScope.launch {
            loadingLive.value = true
            delay(3000)
            val deferredOne = async { get(loginRepoitory.loginUrl) }
            val deferredTwo = async { get(loginRepoitory.loginUrl) }
            deferredOne.await()
            val r = deferredTwo.await()
            r?.let {
                if (it is Result.Success) {
                    loadingLive.value = false
                    messageLive.value = it.data
                }
            }

        }

    }

    fun method6() {
        val job = viewModelScope.launch {
            loadingLive.value = true
            delay(5000)
            loadingLive.value = false
            messageLive.value = "job execute"
        }
        job.cancel()
        messageLive.value = "job cancel"
        loadingLive.value = false
    }


    fun method7() {
        val job = viewModelScope.launch {
            loadingLive.value = true
            delay(5000)
            loadingLive.value = false
            messageLive.value = "job execute"
        }
    }

    fun method8() {
        val scopeMain = CoroutineScope(Job() + Dispatchers.Main)
        val scopeIO = CoroutineScope(Job() + Dispatchers.IO)
        val scopeCPU = CoroutineScope(Job() + Dispatchers.Default)

        scopeMain.launch {
            loadingLive.value = true
            messageLive.value = "scope start"
        }
        scopeMain.launch(Dispatchers.IO) {
            delay(1000)
            val result = async { get(loginRepoitory.loginUrl) }
            val r = result.await()
            scopeIO.launch(Dispatchers.Main) {
                r.let {
                    if (r is Result.Success) {
                        messageLive.value = r.data
                        loadingLive.value = false
                    }
                }
            }

        }

    }


    suspend fun getTitle(): String {
        delay(1000)
        val index = FAKE_RESULTS.size.random()
        return "title: ${index} content:${FAKE_RESULTS[index]} "
    }

    suspend fun getTitle2(): String {
        viewModelScope.launch {
            delay(2_000)
            messageLive.value = getNetworkService().fetchNextTitle()
        }

        return ""
    }

    sealed class Result<out R> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }

    suspend fun get(url: String): Result<String?>? {
        var result: Result<String?>? = null
        withContext(Dispatchers.IO) {
            result = loginRepoitory.makeLoginRequest(url)
        }

        return result
    }

    suspend fun fetchDocs() {
        val result = get(loginRepoitory.loginUrl)
        result?.let {
            if (it is Result.Success) {
                messageLive.value = it.data
            }
        }
    }


    class LoginRepoitory() {
        val loginUrl = "http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=101010100"

        fun makeLoginRequest(url: String = loginUrl): Result<String?> {
            val client = OkHttpClient();
            val request = Request.Builder()
                .url(url)
                .build()
            try {
                val response = client.newCall(request).execute()
                return Result.Success(response.body()?.string())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                return Result.Error(Exception("Cannot open HttpURLConnection"))
            }
        }
    }

    val loginRepoitory = LoginRepoitory()

    suspend fun getWeather() {
        withContext(Dispatchers.IO) {
            val result = loginRepoitory.makeLoginRequest()
            if (result is Result.Success) {
                messageLive.postValue(result.data)
            }
        }
    }

    suspend fun getWeatherWithTimeout() {
        withContext(Dispatchers.IO) {
            withTimeout(1500) {
                val result = loginRepoitory.makeLoginRequest()
                if (result is Result.Success) {
                    messageLive.postValue(result.data)
                }
            }
        }

    }

    suspend fun getWeatherWithTimeout2() {
        withContext(Dispatchers.IO) {
            withTimeout(15000) {
                val result = loginRepoitory.makeLoginRequest()
                if (result is Result.Success) {
                    withContext(Dispatchers.Main) {
                        loadingLive.value = false
                        messageLive.value = result.data
                    }
                }
            }
        }

    }

    suspend fun getWeatherWithTimeoutNotIO() {
        withTimeout(5000) {
            val result = loginRepoitory.makeLoginRequest()
            if (result is Result.Success) {
                messageLive.postValue(result.data)
            }
        }

    }

    suspend fun getNetworkTitle() {
        withContext(Dispatchers.IO) {
            try {
                messageLive.value = getNetworkService().fetchNextTitle()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Unit {
        viewModelScope.launch {
            try {
                messageShowLive.value = false
                block()
            } catch (e: Exception) {
//                messageLive.value = "exception"
                "exception".toast()
            } catch (e: Error) {
//                messageLive.value = "error"
                "error".toast()
                e.printStackTrace()
            } finally {
                messageShowLive.value = true
            }
        }
    }

}
