package com.a.findfragment

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel
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
    data class ListDataModel(val letter: String, val fragmentObject: FragmentAnnotationData)

    fun initVM(intent: Intent?) {
        intent?.let {
            parentName = intent.getStringExtra(PARAM_PARENT_NAME) ?: ""
        }
    }

    override fun loadData(): LiveData<List<ListViewModel.ListDataModel>> {
        if (loading) {
            return itemList
        }
        itemList.value = list
        return itemList
    }

    companion object {
        val list = ArrayList<ListDataModel>()
        fun addAnnotation(annotation: FragmentAnnotation, fragmentName: String) {
            list.add(ListDataModel(annotation.showName, FragmentAnnotationData(annotation.showName, annotation.parentName, fragmentName)))
        }

    }


}