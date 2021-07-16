package com.a.sync.mvvm.fragment

import android.view.View
import com.a.base.RViewPageFragment
import com.a.findfragment.FragmentAnnotation
import com.a.sync.R
import com.a.sync.databinding.FragmentSyncPageBinding
import com.a.sync.mvvm.viewmodel.SyncPageViewModel
import com.bn.utils.string

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
        binding.viewPager.currentItem = 0
    }


    override fun initObserver() {
    }


    private fun initFragments() {
        val d = FragmentData(R.string.sync_tab_main.string(), SyncSimpleFragment())
        fragments.add(d)
        val d2 = FragmentData(R.string.sync_tab_list.string(), SyncListFragment())
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