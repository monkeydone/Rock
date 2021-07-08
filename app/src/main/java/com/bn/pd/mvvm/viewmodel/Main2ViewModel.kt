package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class Main2ViewModel(application: Application) :
    BaseViewModel<List<Main2ViewModel.Main2DataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class Main2DataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
