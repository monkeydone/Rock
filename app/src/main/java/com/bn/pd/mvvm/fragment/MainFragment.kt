package com.bn.pd.mvvm.fragment

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.a.base.RViewPageFragment
import com.a.base.TabEntity
import com.a.findfragment.EmptyFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentMainBinding
import com.bn.pd.mvvm.viewmodel.MainViewModel
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener

@FragmentAnnotation("Main", "Template")
class MainFragmentFragment : RViewPageFragment<MainViewModel, FragmentMainBinding>(),
    View.OnClickListener {

    private val fragments = ArrayList<FragmentData>()

    private val mTitles = arrayOf("首页", "消息", "联系人", "更多")
    private val mIconUnselectIds = intArrayOf(
        R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
        R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect
    )
    private val mIconSelectIds = intArrayOf(
        R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
        R.mipmap.tab_contact_select, R.mipmap.tab_more_select
    )

    private val mTabEntities = java.util.ArrayList<CustomTabEntity>()

    override fun getContentId() = R.layout.fragment_main
    override fun getStoneId() = 0


    override fun initIntent() {
        initFragments()
    }

    override fun initBinding() {
        binding.onClickListener = this
    }

    override fun initView() {
        binding.viewPager.currentItem = 1
        initTabLayout()

    }

    private fun initTabLayout() {
        for (i in mTitles.indices) {
            mTabEntities.add(TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]))
        }
        binding.tabLayout.setTabData(mTabEntities)
        binding.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                binding.viewPager.setCurrentItem(position)
            }

            override fun onTabReselect(position: Int) {
                if (position == 0) {
//                    binding.tabLayout.showMsg(0, mRandom.nextInt(100) + 1)
                    //                    UnreadMsgUtils.show(binding.tabLayout.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        })

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.tabLayout.setCurrentTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        binding.viewPager.setCurrentItem(1)

    }

    override fun initObserver() {
    }


    private fun initFragments() {
        for (i in mTitles.indices) {
            val title = mTitles[i]
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
//        binding.tabLayout.setViewPager(viewPager)
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