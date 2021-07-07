package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.NoViewModel
import com.a.base.RViewPageFragment
import com.a.findfragment.EmptyFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentImagePageBinding

@FragmentAnnotation("ImagePage", "Template")
class ImagePageFragment : RViewPageFragment<NoViewModel, FragmentImagePageBinding>(),
    View.OnClickListener {

    private val fragments = ArrayList<FragmentData>()

    override fun getContentId() = R.layout.fragment_image_page

    override fun getStoneId() = 0


    override fun initIntent() {
        initFragments()
    }

    override fun initBinding() {
        binding.onclickListener = this
    }

    override fun initView() {
//        fragments.forEach {
//            binding.tabLayout.addNewTab(it.title)
//        }
        //不同入口进入选中不同tab
        binding.viewPager.currentItem = 1


    }


    override fun initObserver() {
    }


    private fun initFragments() {
        for (i in 0..3) {
            val title = "title${i}"
            val d = FragmentData(title, EmptyFragment())
            fragments.add(d)
        }
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