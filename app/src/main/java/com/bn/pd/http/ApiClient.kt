package com.bn.pd.http

import com.baronzhang.retrofit2.converter.FastJsonConverterFactory
import com.bn.pd.BuildConfig
import com.bn.pd.http.service.WeatherService
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object ApiClient {
    lateinit var weatherService: WeatherService

    fun init() {
        val weatherApiHost = ApiConstants.MI_WEATHER_API_HOST
        ApiClient.weatherService = initWeatherService<WeatherService>(
            weatherApiHost,
            WeatherService::class.java
        )
    }

    private fun <T> initWeatherService(baseUrl: String, clazz: Class<T>): T {
        val builder = OkHttpClient().newBuilder()
        if (BuildConfig.DEBUG) {
//            val httpLoggingInterceptor = HttpLoggingInterceptor()
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//            builder.addInterceptor(httpLoggingInterceptor)
            //            builder.addNetworkInterceptor(new StethoInterceptor());
//            BuildConfig.STETHO.addNetworkInterceptor(builder)
        }
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(FastJsonConverterFactory.create())
//            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(client)
            .build()
        return retrofit.create(clazz)
    }
}