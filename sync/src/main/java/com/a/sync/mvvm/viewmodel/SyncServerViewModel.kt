package com.a.sync.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class SyncServerViewModel(application: Application) :
    BaseViewModel<List<SyncServerViewModel.SyncServerDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class SyncServerDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
