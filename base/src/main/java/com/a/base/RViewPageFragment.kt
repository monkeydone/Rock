package com.a.base

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager


abstract class RViewPageFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> :
    RBaseFragment<VM, DB>() {

    protected lateinit var adapter: FragmentPagerAdapter
    private lateinit var fragments: List<Fragment>
    private lateinit var titles: List<Int>


    data class FragmentData(val titleResId: Int, val fragment: Fragment)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBinding()
        adapter = Adapter(childFragmentManager)
        fragments = getFragments()
        titles = getTitles()

        getViewPager().adapter = adapter
        getViewPager().offscreenPageLimit = getViewPagerLimit()
        bindViewPager(getViewPager())
        initView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isAdded) {
            for (fragment in childFragmentManager.fragments) {
                if (fragment is RBaseFragment<*, *>) {
//                    fragment.isPageVisible = isVisibleToUser
                    fragment.userVisibleHint = isVisibleToUser
                } else {
                    fragment.userVisibleHint = isVisibleToUser
                }
            }
        }
        super.setUserVisibleHint(isVisibleToUser)
    }

    abstract fun getFragments(): List<Fragment>
    abstract fun getTitles(): List<Int>
    abstract fun getViewPager(): ViewPager
    abstract fun bindViewPager(viewPager: ViewPager)

    open fun getViewPagerLimit(): Int = 2

    inner class Adapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        private val adapterFragments = SparseArray<Fragment>()

        override fun getCount(): Int = fragments.size

        override fun getItem(p0: Int): Fragment = fragments[p0]

        override fun getPageTitle(position: Int): CharSequence? = getString(titles[position])

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as Fragment
            adapterFragments.put(position, fragment)
            return fragment
        }

        fun getFragment(position: Int): Fragment = adapterFragments.get(position)
    }
}
