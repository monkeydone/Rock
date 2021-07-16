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
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                viewModel.onMainViewClicked()
            }

        }
    }


}