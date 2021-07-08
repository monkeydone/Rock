package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel


class PopupViewModel(application: Application) :
    BaseViewModel<List<PopupViewModel.PopupDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class PopupDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
