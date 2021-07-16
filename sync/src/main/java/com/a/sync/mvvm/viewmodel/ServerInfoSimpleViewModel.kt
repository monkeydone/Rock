package com.a.sync.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class ServerInfoSimpleViewModel(application: Application) :
    BaseViewModel<List<ServerInfoSimpleViewModel.ServerInfoSimpleDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class ServerInfoSimpleDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
