package com.a.videoplayer.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class VideoPlayerViewModel(application: Application) :
    BaseViewModel<List<VideoPlayerViewModel.VideoPlayerDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class VideoPlayerDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L


    fun getPlayUrl(): String? {
        return "http://10.129.100.241:8000/m3"
//        return  this.intent.getStringExtra(PARAM_URL)
    }

    companion object {
        const val PARAM_URL = "url"
    }

}
