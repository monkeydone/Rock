package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.base.funOwnerObserver
import com.a.base.list.SimpleGridDecoration
import com.a.base.list.loadListData

import com.a.base.observer

import com.a.findfragment.FragmentAnnotation

import com.art.ui.adapter.recyclerview.CommonAdapter

import com.bn.pd.R
import com.bn.pd.databinding.FragmentList1Binding
import com.bn.pd.mvvm.viewmodel.List1ViewModel

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener

@FragmentAnnotation("List1", "Template")
class List1Fragment : RBaseFragment<List1ViewModel, FragmentList1Binding>(), OnItemClickListener,
    OnLoadMoreListener {

    private val adapter =
        CommonAdapter<List1ViewModel.List1DataModel>(R.layout.fragment_item_list1).apply {
            setOnItemClickListener(this@List1Fragment)
            loadMoreModule.setOnLoadMoreListener(this@List1Fragment)
        }

    override fun getContentId(): Int = R.layout.fragment_list1

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