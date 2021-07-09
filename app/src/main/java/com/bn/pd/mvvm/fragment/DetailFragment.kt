package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentDetailBinding
import com.bn.pd.mvvm.viewmodel.DetailViewModel
import com.bn.utils.fillFragment


@FragmentAnnotation("Detail", "Template")
class DetailFragment : RBaseFragment<DetailViewModel, FragmentDetailBinding>(), View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_detail

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
        initScrollContainer(true)
    }

    private fun initScrollContainer(isList: Boolean) {
        if (isList) {
            R.id.fl_scroll_container.fillFragment(ImageListFragment(), childFragmentManager)
        } else {
            R.id.fl_scroll_container.fillFragment(ImagePageFragment(), childFragmentManager)
        }
    }

    override fun initData() {
        viewModel.loadData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                initScrollContainer(true)
            }
            R.id.tv_event -> {
                initScrollContainer(false)
            }

        }
    }


}
