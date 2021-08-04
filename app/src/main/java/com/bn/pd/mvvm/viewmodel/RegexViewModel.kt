package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class RegexViewModel(application: Application) :
    BaseViewModel<List<RegexViewModel.RegexDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class RegexDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
