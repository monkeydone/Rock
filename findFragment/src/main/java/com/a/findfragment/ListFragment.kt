package com.a.findfragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.base.funOwnerObserver
import com.a.base.list.loadListData
import com.a.base.observer
import com.a.findfragment.databinding.FragmentListBinding
import com.art.ui.adapter.recyclerview.CommonAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener


class ListFragment : RBaseFragment<ListViewModel, FragmentListBinding>(), OnItemClickListener,
    OnLoadMoreListener {

    private val artworkAdapter =
        CommonAdapter<ListViewModel.ListDataModel>(R.layout.fragment_item_list).apply {
            setOnItemClickListener(this@ListFragment)
            loadMoreModule.setOnLoadMoreListener(this@ListFragment)
        }

    override fun getContentId(): Int = R.layout.fragment_list

    override fun initIntent() {
        viewModel.initVM(requireActivity().intent)
    }

    override fun initObserver() {
        observer(viewModel.noDataLive) {
//            binding.flNoData.visibility = if (it == true) View.VISIBLE else View.GONE
        }
    }

    override fun initView() {
        binding.recyclerView.adapter = artworkAdapter
//        binding.recyclerView.addItemDecoration(SpaceItemDecoration(flank = 10.dp, spanFlank = 16.dp, spanInterval = 8.dp))

        initAllView()
    }

    /**
     * 根据不同Type 展示不同UI
     */
    private fun initAllView() {
//        when (viewModel.type) {
//            ArtWorkListType.ARTWORK_ARTWORK -> {
//                R.layout.item_artist_header.layoutToDataBinding().apply {
//                    (this as ItemArtistHeaderBinding).msg = viewModel.msg
//                    artworkAdapter.addHeaderView(this.root)
//                }
//            }
//            else -> {
//            }
//        }
    }

    override fun initData() {
        getGalleryArtWorks(true)
    }

    override fun onLoadMore() {
        getGalleryArtWorks(false)
    }

    private fun getGalleryArtWorks(isFirst: Boolean) {
        funOwnerObserver(viewModel.loadData()) {
            loadListData(binding.recyclerView, artworkAdapter, it, isFirst)
        }
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val it = artworkAdapter.data[position].fragmentObject
        ListActivity.startFragment(binding.root.context, it.fragmentName, it.showName)
    }


}