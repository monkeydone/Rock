package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel

class List1ViewModel(application: Application) :
    BaseViewModel<List<List1ViewModel.List1DataModel>>(application) {

    var dataLive = MutableLiveData<Boolean>()
    var loading = false
    var id = 0L

    init {
        canReload = true
        canLoadMore = false
    }

    data class List1DataModel(val letter: String, val position: Int)


    override fun loadData(): LiveData<List<List1ViewModel.List1DataModel>> {
        if (loading) {
            return itemList
        }
        val list = ArrayList<List1ViewModel.List1DataModel>()
        val size = itemList.value?.size ?: 1

        if (canReload) {
            for (i in 0..50) {
                val model = List1ViewModel.List1DataModel("Item: ${i}", i)
                list.add(model)
            }
        } else if (canLoadMore) {
            for (i in size..size + 50) {
                val model = List1ViewModel.List1DataModel("Item: ${i}", i)
                list.add(model)
            }
        }
        canLoadMore = size <= 150
        //itemList.value = handleListRequestResponse(list=list,haveMore = canLoadMore)
        itemList.value = list
        return itemList
    }


}