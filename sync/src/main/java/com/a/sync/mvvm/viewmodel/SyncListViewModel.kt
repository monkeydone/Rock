package com.a.sync.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel

class SyncListViewModel(application: Application) :
    BaseViewModel<List<SyncListViewModel.SyncListDataModel>>(application) {

    var dataLive = MutableLiveData<Boolean>()
    var loading = false
    var id = 0L

    init {
        canReload = true
        canLoadMore = false
    }

    data class SyncListDataModel(val letter: String, val position: Int)


    override fun loadData(): LiveData<List<SyncListViewModel.SyncListDataModel>> {
        if (loading) {
            return itemList
        }
        val list = ArrayList<SyncListViewModel.SyncListDataModel>()
        val size = itemList.value?.size ?: 1

        if (canReload) {
            for (i in 0..50) {
                val model = SyncListViewModel.SyncListDataModel("Item: ${i}", i)
                list.add(model)
            }
        } else if (canLoadMore) {
            for (i in size..size + 50) {
                val model = SyncListViewModel.SyncListDataModel("Item: ${i}", i)
                list.add(model)
            }
        }
        canLoadMore = size <= 150
        //itemList.value = handleListRequestResponse(list=list,haveMore = canLoadMore)
        itemList.value = list
        return itemList
    }


}