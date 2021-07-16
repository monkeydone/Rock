package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.base.observer
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentCoroutinesBinding
import com.bn.pd.mvvm.viewmodel.CoroutinesViewModel


@FragmentAnnotation("Coroutines", "Demo")
class CoroutinesFragment : RBaseFragment<CoroutinesViewModel, FragmentCoroutinesBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_coroutines

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
            binding.tvEvent.text = it
        }

        observer(viewModel.messageLive) {
            binding.tvRandomText.text = it
        }

        observer(viewModel.messageShowLive) {
            binding.tvRandomText.visibility = if (it == true) View.VISIBLE else View.GONE
        }

        observer(viewModel.loadingLive) {
            if (it == true) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                viewModel.dalay1000()
            }
            R.id.tv_method1 -> {
                viewModel.method1()
            }
            R.id.tv_method2 -> {
                viewModel.method2()
            }
            R.id.tv_method3 -> {
                viewModel.method3()
            }
            R.id.tv_method4 -> {
                viewModel.method4()
            }
            R.id.tv_method5 -> {
                viewModel.method5()
            }
            R.id.tv_method6 -> {
                viewModel.method6()
            }
            R.id.tv_method7 -> {
                viewModel.method7()
            }
            R.id.tv_method8 -> {
                viewModel.method8()
            }
        }

    }


}
