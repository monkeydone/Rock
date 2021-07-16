package com.a.sync.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class SyncPageViewModel(application: Application) :
    BaseViewModel<List<SyncPageViewModel.SyncPageDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class SyncPageDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
