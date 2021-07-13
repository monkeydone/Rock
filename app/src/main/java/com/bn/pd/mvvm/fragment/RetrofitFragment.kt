package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.base.observer
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentRetrofitBinding
import com.bn.pd.mvvm.viewmodel.RetrofitViewModel


@FragmentAnnotation("Retrofit", "Demo")
class RetrofitFragment : RBaseFragment<RetrofitViewModel, FragmentRetrofitBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_retrofit

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
    }

    override fun initData() {
        viewModel.loadData()
    }

    override fun initObserver() {
        super.initObserver()
        observer(viewModel.dataLive) {
            hideLoadingDialog()
            binding.tvEvent.text = it.toString()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
//                "message".toast()
                viewModel.getWeather()
                showLoadingDialog()
            }

        }
    }


}
