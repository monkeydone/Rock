package com.a.findfragment

import android.app.Application
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel
import com.a.dproject.FragmentObject
import com.a.dproject.getAnnotationMap
import com.a.findfragment.ListActivity.Companion.PARAM_PARENT_NAME

class ListViewModel(application: Application) :
    BaseViewModel<List<ListViewModel.ListDataModel>>(application) {

    var dataLive = MutableLiveData<Boolean>()
    var loading = false
    var id = 0L
    var parentName = ""

    init {
        canReload = true
        canLoadMore = false
    }

    //    data class ListDataModel(val letter: String)
    data class ListDataModel(val letter: String, val fragmentObject: FragmentObject)

    fun initVM(intent: Intent?) {
        intent?.let {
            parentName = intent.getStringExtra(PARAM_PARENT_NAME) ?: ""
        }
    }

    override fun loadData(): LiveData<List<ListViewModel.ListDataModel>> {
        if (loading) {
            return itemList
        }
        val list = ArrayList<ListDataModel>()
        getAnnotationMap().forEach {
            val key = if (TextUtils.isEmpty(it.key)) {
                it.value
            } else {
                it.key
            }
            if (parentName == it.value.parentName) {
                list.add(ListDataModel(it.value.showName, it.value))
            }
        }
//        list.add(ListDataModel("SimpleFragment", "SimpleFragment"))
//        list.add(ListDataModel("TabFragment", "TabFragment"))
        //itemList.value = handleListRequestResponse(list=list,haveMore = canLoadMore)
        itemList.value = list
        return itemList
    }

    companion object {
    }


}