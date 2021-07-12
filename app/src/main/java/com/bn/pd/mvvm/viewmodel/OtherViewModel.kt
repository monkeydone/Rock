package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class OtherViewModel(application: Application) :
    BaseViewModel<List<OtherViewModel.OtherDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class OtherDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
