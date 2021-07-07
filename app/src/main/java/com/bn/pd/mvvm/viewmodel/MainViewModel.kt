package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class MainViewModel(application: Application) :
    BaseViewModel<List<MainViewModel.MainDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class MainDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
