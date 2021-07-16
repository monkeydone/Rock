package com.a.sync.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class SyncSimpleViewModel(application: Application) :
    BaseViewModel<List<SyncSimpleViewModel.SyncSimpleDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class SyncSimpleDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
