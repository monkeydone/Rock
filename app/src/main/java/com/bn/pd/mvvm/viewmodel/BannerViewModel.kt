package com.bn.pd.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.base.BaseViewModel
import java.util.*


class BannerViewModel(application: Application) :
    BaseViewModel<List<BannerViewModel.BannerDataModel>>(
        application
    ) {
    var dataLive = MutableLiveData<Boolean>()

    data class BannerDataModel(val titleResId: Int, val fragment: Fragment)

    data class DataBean(val imageUrl: String, val title: String, val viewType: Int)

    var id = 0L

    fun getMarqueeData(): MutableList<String> {

        val messages: MutableList<String> = ArrayList()
        messages.add("1. 大家好，我是孙福生。")
        messages.add("2. 欢迎大家关注我哦！")
        messages.add("3. GitHub帐号：sunfusheng")
        messages.add("4. 新浪微博：孙福生微博")
        messages.add("5. 个人博客：sunfusheng.com")
        messages.add("6. 微信公众号：孙福生")
        return messages
    }

    fun getTestData3(): List<DataBean>? {
        val list: MutableList<DataBean> =
            ArrayList<DataBean>()
        list.add(
            DataBean(
                "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
                "",
                1
            )
        )
        list.add(
            DataBean(
                "https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg",
                "",
                1
            )
        )
        list.add(
            DataBean(
                "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg",
                "",
                1
            )
        )
        list.add(
            DataBean(
                "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
                "",
                1
            )
        )
        list.add(
            DataBean(
                "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg",
                "",
                1
            )
        )
        return list
    }

}
