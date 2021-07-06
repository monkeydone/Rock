package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.base.funOwnerObserver
import com.a.base.list.SimpleGridDecoration
import com.a.base.list.DelegateMultiAdapter

import com.a.base.list.loadListData

import com.a.base.observer

import com.a.findfragment.FragmentAnnotation

import com.art.ui.adapter.recyclerview.CommonAdapter

import com.bn.pd.R
import com.bn.pd.databinding.FragmentListMutilTypeBinding
import com.bn.pd.mvvm.viewmodel.ListMutilTypeViewModel

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate

@FragmentAnnotation("ListMutilType","Template")
class ListMutilTypeFragment  : RBaseFragment<ListMutilTypeViewModel, FragmentListMutilTypeBinding>(), OnItemClickListener,
    OnLoadMoreListener {

 
    private val adapter =
        DelegateMultiAdapter<ListMutilTypeViewModel.ListMutilTypeDataModel>().apply {
            setOnItemClickListener(this@ListMutilTypeFragment)
            setMultiTypeDelegate(ListMutilTypeDelegate ())
            loadMoreModule.setOnLoadMoreListener(this@ListMutilTypeFragment)
        }


    class ListMutilTypeDelegate : BaseMultiTypeDelegate<ListMutilTypeViewModel.ListMutilTypeDataModel>() {

        init {
            addItemType(
                ListMutilTypeViewModel.ListMutilTypeDataModel.DataType.TYPE_SINGLE.value,
                R.layout.fragment_header_list_mutil_type
            )
            addItemType(
                ListMutilTypeViewModel.ListMutilTypeDataModel.DataType.TYPE_NORMAL.value,
                R.layout.fragment_item_list_mutil_type
            )
        }

        override fun getItemType(data: List<ListMutilTypeViewModel.ListMutilTypeDataModel>, position: Int): Int {
            return data[position].type.value
        }


    }


    override fun getContentId(): Int = R.layout.fragment_list_mutil_type

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