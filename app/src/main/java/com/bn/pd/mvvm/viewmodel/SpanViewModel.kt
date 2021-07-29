package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class SpanViewModel(application: Application) :
    BaseViewModel<List<SpanViewModel.SpanDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class SpanDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
