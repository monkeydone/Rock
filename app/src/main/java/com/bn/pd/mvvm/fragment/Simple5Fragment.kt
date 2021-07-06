package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentSimple5Binding
import com.bn.pd.mvvm.viewmodel.Simple5ViewModel
import com.bn.utils.toast


@FragmentAnnotation("Simple5", "Template")
class Simple5Fragment : RBaseFragment<Simple5ViewModel, FragmentSimple5Binding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_simple5

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
    }

    override fun initData() {
        viewModel.loadData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                "message".toast()
            }

        }
    }


}
