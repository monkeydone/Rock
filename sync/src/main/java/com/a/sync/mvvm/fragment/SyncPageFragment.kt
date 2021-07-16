package com.a.sync.mvvm.fragment

import android.view.View
import com.a.base.RViewPageFragment
import com.a.findfragment.FragmentAnnotation
import com.a.sync.R
import com.a.sync.databinding.FragmentSyncPageBinding
import com.a.sync.mvvm.viewmodel.SyncPageViewModel

@FragmentAnnotation("SyncPage", "Sync")
class SyncPageFragmentFragment : RViewPageFragment<SyncPageViewModel, FragmentSyncPageBinding>(),
    View.OnClickListener {

    private val fragments = ArrayList<FragmentData>()

    override fun getContentId() = R.layout.fragment_sync_page
    override fun getStoneId() = 0


    override fun initIntent() {
        initFragments()
    }

    override fun initBinding() {
        binding.onClickListener = this
    }

    override fun initView() {
        binding.viewPager.currentItem = 1
    }


    override fun initObserver() {
    }


    private fun initFragments() {
//        for (i in 0..2) {
//            val title = "title${i}"
//
//        }
        val d = FragmentData("主页", SyncSimpleFragment())
        fragments.add(d)
        val d2 = FragmentData("消息列表", SyncListFragment())
        fragments.add(d2)
    }


    override fun getFragments(): List<FragmentData> {
        return fragments
    }


    override fun getViewPager(): androidx.viewpager.widget.ViewPager {
        return binding.root.findViewById(R.id.view_pager)
    }

    override fun bindViewPager(viewPager: androidx.viewpager.widget.ViewPager) {
        binding.tabLayout.setViewPager(viewPager)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.iv_back -> {
//                consumeBackPress()
//            }
        }

    }

    override fun initData() {
    }


}