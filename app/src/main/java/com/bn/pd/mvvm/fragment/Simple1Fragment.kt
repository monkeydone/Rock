package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.NoViewModel
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation

import com.bn.pd.R
import com.bn.pd.databinding.FragmentSimple1Binding
import com.bn.utils.toast


@FragmentAnnotation("Simple", "Template")
class Simple1Fragment : RBaseFragment<NoViewModel, FragmentSimple1Binding>(), View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_simple1

    override fun initView() {
        binding.onClickListener = this
    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                "message".toast()
            }

        }
    }


}
