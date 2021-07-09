package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel
import com.bn.utils.random

class ImageListViewModel(application: Application) :
        BaseViewModel<List<ImageListViewModel.List1DataModel>>(application) {

    var dataLive = MutableLiveData<Boolean>()
    var loading = false
    var id = 0L
    val imageUrlList = initUrlList()

    init {
        canReload = true
        canLoadMore = false
    }

    private fun initUrlList(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2279952540,2544282724&fm=26&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=851052518,4050485518&fm=26&gp=0.jpg")
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=174904559,2874238085&fm=26&gp=0.jpg")
        list.add("https://user-gold-cdn.xitu.io/2019/1/25/168839e977414cc1?imageView2/2/w/800/q/100")
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551692956639&di=8ee41e070c6a42addfc07522fda3b6c8&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160413%2F75659e9b05b04eb8adf5b52669394897.jpg")
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/4B4E81902BF3B6285DFAC5EAD2C3A9F3.jpg")
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/B40CF2CA54715E64CF4AA3632FD4F70E.jpg")
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/C2A333BA3CCCBE8290E2F9549385E0C1.jpg")
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/3F8B1BFDCBA2559EB69BA1670915E912.jpg")
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/5C50B56D6FC9C30562FE15716B02AA3E.jpg")
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/E211145D8BA5CC519E9ED56D1AC57D2A.jpg")
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/92FA62C554C0A4B61251A5A2FCDD400B.jpg")
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/7ECFF80AEDFF9D2771DAFB979D13513E.jpg")
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/C12F6B62FF052BAB4844AB9A5A333F3C.jpg")

        return list
    }

    data class List1DataModel(val letter: String, val position: Int, val url: String)


    override fun loadData(): LiveData<List<ImageListViewModel.List1DataModel>> {
        if (loading) {
            return itemList
        }
        val list = ArrayList<ImageListViewModel.List1DataModel>()
        val size = itemList.value?.size ?: 1

        if (canReload) {
            for (i in 0..50) {
                val model = ImageListViewModel.List1DataModel(
                        "Item: ${i}",
                        i,
                        imageUrlList[imageUrlList.size.random()]
                )
                list.add(model)
            }
        } else if (canLoadMore) {
            for (i in size..size + 50) {
                val model = ImageListViewModel.List1DataModel(
                        "Item: ${i}",
                        i,
                        imageUrlList[imageUrlList.size.random()]
                )
                list.add(model)
            }
        }
        canLoadMore = size <= 150
        //itemList.value = handleListRequestResponse(list=list,haveMore = canLoadMore)
        itemList.value = list
        return itemList
    }


}