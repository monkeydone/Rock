package com.a.sync.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.sync.R
import com.a.sync.databinding.FragmentSyncSimpleBinding
import com.a.sync.mvvm.viewmodel.SyncSimpleViewModel
import com.bn.utils.toast


class SyncSimpleFragment : RBaseFragment<SyncSimpleViewModel, FragmentSyncSimpleBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_sync_simple

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
