package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel

class ListMutilTypeViewModel(application: Application) : BaseViewModel<List<ListMutilTypeViewModel.ListMutilTypeDataModel>>(application)  {

    var dataLive = MutableLiveData<Boolean>()
    var loading = false
    var id = 0L

    init {
        canReload = true
        canLoadMore = false
    }

 
    data class ListMutilTypeDataModel(val letter: String, val position: Int,val type:DataType){
        enum class DataType(val value:Int) {
            TYPE_NORMAL(0),
            TYPE_SINGLE(1)
        }
    }

    fun getRandomType(position: Int):ListMutilTypeDataModel.DataType {
       if(position % 2 == 0){
           return ListMutilTypeDataModel.DataType.TYPE_NORMAL
       }else {
           return ListMutilTypeDataModel.DataType.TYPE_SINGLE
       }
    }
 
    override fun loadData(): LiveData<List<ListMutilTypeViewModel.ListMutilTypeDataModel>> {
        if (loading) {
            return itemList
        }
        val list = ArrayList<ListMutilTypeViewModel.ListMutilTypeDataModel>()
        val size = itemList.value?.size?:1

        if(canReload) {
            for (i in 0..50) {
                val model = ListMutilTypeViewModel.ListMutilTypeDataModel("Item: ${i}", i,getRandomType(i))
                list.add(model)
            }
        }else if(canLoadMore) {
            for(i in size..size+50){
                val model = ListMutilTypeViewModel.ListMutilTypeDataModel("Item: ${i}", i,getRandomType(i))
                list.add(model)
            }
        }
        canLoadMore = size <= 150
        //itemList.value = handleListRequestResponse(list=list,haveMore = canLoadMore)
        itemList.value = list
        return itemList
    }

        
}