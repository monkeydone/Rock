package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class StatusBarViewModel(application: Application) :
    BaseViewModel<List<StatusBarViewModel.StatusBarDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class StatusBarDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
