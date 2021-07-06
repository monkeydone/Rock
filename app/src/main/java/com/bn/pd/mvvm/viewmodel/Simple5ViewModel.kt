package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class Simple5ViewModel(application: Application) :
    BaseViewModel<List<Simple5ViewModel.Simple5DataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class Simple5DataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
