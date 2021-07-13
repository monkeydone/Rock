package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel
import com.bn.pd.http.ApiClient
import com.bn.pd.http.entity.mi.MiWeather
import com.bn.utils.toast
import retrofit2.Call
import retrofit2.Response


class RetrofitViewModel(application: Application) :
    BaseViewModel<List<RetrofitViewModel.RetrofitDataModel>>(application) {
    var dataLive = MutableLiveData<MiWeather>()

    data class RetrofitDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L


    fun getWeather() {
        val result = ApiClient.weatherService.getMiWeather("101010100")
        result.enqueue(object : retrofit2.Callback<MiWeather> {
            override fun onResponse(call: Call<MiWeather>, response: Response<MiWeather>) {
                response.body()?.let {

                    dataLive.value = it
                }

            }

            override fun onFailure(call: Call<MiWeather>, t: Throwable) {
                "error".toast()
            }

        })

    }
}
