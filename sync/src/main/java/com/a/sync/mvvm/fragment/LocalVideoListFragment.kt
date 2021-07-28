package com.a.sync.mvvm.fragment

import android.Manifest
import android.util.Log
import android.view.View
import com.a.base.RBaseFragment
import com.a.base.funOwnerObserver
import com.a.base.list.SimpleGridDecoration
import com.a.base.list.loadListData
import com.a.base.observer
import com.a.findfragment.FragmentAnnotation
import com.a.sync.R
import com.a.sync.Utils
import com.a.sync.databinding.FragmentLocalVideoListBinding
import com.a.sync.mvvm.viewmodel.LocalVideoListViewModel
import com.a.videoplayer.mvvm.fragment.VideoPlayerFragment
import com.art.ui.adapter.recyclerview.CommonAdapter
import com.bn.utils.PermissionUtils
import com.bn.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.permissionx.guolindev.PermissionX

@FragmentAnnotation("LocalVideoList", "Sync")
class LocalVideoListFragment :
    RBaseFragment<LocalVideoListViewModel, FragmentLocalVideoListBinding>(), OnItemClickListener,
    OnLoadMoreListener {

    private val adapter =
        CommonAdapter<LocalVideoListViewModel.LocalVideoListDataModel>(R.layout.fragment_item_local_video_list).apply {
            setOnItemClickListener(this@LocalVideoListFragment)
            loadMoreModule.setOnLoadMoreListener(this@LocalVideoListFragment)
        }

    override fun getContentId(): Int = R.layout.fragment_local_video_list

    override fun initIntent() {
//        viewModel.initVM(requireActivity().intent)
    }

    override fun initObserver() {
        observer(viewModel.noDataLive) {
//            binding.flNoData.visibility = if (it == true) View.VISIBLE else View.GONE
        }

        funOwnerObserver(viewModel.itemList) {
//            LocalVideoListViewModel.fileList.addAll(it)
            loadListData(binding.recyclerView, adapter, it, true)
        }
    }

    override fun initView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(SimpleGridDecoration(10))

    }

    private fun initRequestPermission(callback: () -> Unit) {
        if (!PermissionUtils.hasSelfPermissions(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            PermissionX.init(activity)
                .permissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        callback()
                        "All permissions are granted".toast()
                    } else {
                        "These permissions are denied: $deniedList".toast()
                    }
                }
        } else {
            callback()
        }

    }

    override fun initData() {
        initRequestPermission {
            viewModel.loadData()
        }
    }

    override fun onLoadMore() {
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val item = adapter.data[position] as LocalVideoListViewModel.LocalVideoListDataModel
        val url = Utils.getUrlForFile(item.fullName)
        Log.e(TAG, "url :${url}")
        VideoPlayerFragment.openVideo(url)
//        val it = artworkAdapter.data[position].fragmentObject
    }

    companion object {
        var TAG = LocalVideoListFragment.Companion.javaClass.canonicalName
    }

}