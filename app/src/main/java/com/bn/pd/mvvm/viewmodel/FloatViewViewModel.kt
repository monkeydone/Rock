package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class FloatViewViewModel(application: Application) :
    BaseViewModel<List<FloatViewViewModel.FloatViewDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class FloatViewDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
