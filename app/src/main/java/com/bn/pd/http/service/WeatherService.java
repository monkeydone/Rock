package com.bn.pd.http.service;


import com.bn.pd.http.entity.know.KnowWeather;
import com.bn.pd.http.entity.mi.MiWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 * 16/2/25
 */
public interface WeatherService {

    /**
     * http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=101010100
     *
     * @param cityId 城市ID
     * @return 天气数据
     */
    @GET("weather")
    Call<MiWeather> getMiWeather(@Query("cityId") String cityId);


    /**
     * http://knowweather.duapp.com/v1.0/weather/101010100
     *
     * @param cityId 城市ID
     * @return 天气数据
     */
    @GET("v1.0/weather/{cityId}")
    Call<KnowWeather> getKnowWeather(@Path("cityId") String cityId);


}
