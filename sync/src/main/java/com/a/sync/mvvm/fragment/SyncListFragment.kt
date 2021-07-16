package com.a.sync.mvvm.fragment

import android.view.Gravity
import android.view.View
import androidx.databinding.ViewDataBinding
import com.a.base.RBaseFragment
import com.a.base.funOwnerObserver
import com.a.base.list.SimpleGridDecoration
import com.a.base.list.loadListData
import com.a.base.observer
import com.a.sync.R
import com.a.sync.WSMode
import com.a.sync.databinding.FragmentItemSyncListBinding
import com.a.sync.databinding.FragmentSyncListBinding
import com.a.sync.mvvm.viewmodel.SyncListViewModel
import com.art.ui.adapter.recyclerview.CommonAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class SyncListFragment : RBaseFragment<SyncListViewModel, FragmentSyncListBinding>(),
    OnItemClickListener,
    OnLoadMoreListener {

    private val adapter =
        object :
            CommonAdapter<SyncListViewModel.SyncListDataModel>(R.layout.fragment_item_sync_list) {
            override fun convert(
                holder: BaseDataBindingHolder<ViewDataBinding>,
                item: SyncListViewModel.SyncListDataModel
            ) {
                super.convert(holder, item)
                val binding = holder.dataBinding as FragmentItemSyncListBinding
                if (item.who == WSMode.HOST) {
                    binding.tvLetter.text = "${item.who}  ${item.message}"
                    binding.tvLetter.gravity = Gravity.LEFT
                } else {
                    binding.tvLetter.text = "${item.message} ${item.who}"
                    binding.tvLetter.gravity = Gravity.RIGHT
                }
            }
        }.apply {
            setOnItemClickListener(this@SyncListFragment)
            loadMoreModule.setOnLoadMoreListener(this@SyncListFragment)
        }

    override fun getContentId(): Int = R.layout.fragment_sync_list

    override fun initIntent() {
//        viewModel.initVM(requireActivity().intent)
    }

    override fun initObserver() {
        observer(viewModel.noDataLive) {
//            binding.flNoData.visibility = if (it == true) View.VISIBLE else View.GONE
        }
    }

    override fun initView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(SimpleGridDecoration(10))

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            initData()
        }
    }

    override fun initData() {
        getMoreData(true)
    }

    override fun onLoadMore() {
        getMoreData(false)
    }

    private fun getMoreData(isFirst: Boolean) {
        funOwnerObserver(viewModel.loadData()) {
            loadListData(binding.recyclerView, adapter, it, isFirst)
        }
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
//        val it = artworkAdapter.data[position].fragmentObject
    }


}