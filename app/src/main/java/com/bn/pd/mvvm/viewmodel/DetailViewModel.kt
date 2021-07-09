package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class DetailViewModel(application: Application) : BaseViewModel<List<DetailViewModel.DetailDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class DetailDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
