package com.a.base.list

import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


data class ListData<T>(val list: List<T>, val hasMore: Boolean)

fun <T> listData(list: List<T>, hasMore: Boolean) = ListData(list, hasMore)
fun <T> emptyListData() = ListData(emptyList<T>(), false)

fun <T, K : BaseViewHolder> loadListData(
    recyclerView: RecyclerView,
    adapter: BaseQuickAdapter<T, K>,
    data: ListData<T>,
    isFirst: Boolean,
    pageCount: Int = 20,
    loadMoreEnd: Boolean = false,
    adapterState: AdapterUtils.AdapterState? = null
) {
    loadListData(
        recyclerView,
        adapter,
        data.list,
        isFirst,
        pageCount,
        loadMoreEnd,
        data.hasMore,
        adapterState
    )
}

/**
 * 上拉加载-通用工具类
 *
 * @param isFirst      是否是第一次加载数据
 * @param adapterState 用于给界面做不同状态的处理
 */
fun <T, K : BaseViewHolder> loadListData(
    recyclerView: RecyclerView,
    adapter: BaseQuickAdapter<T, K>,
    data: List<T>,
    isFirst: Boolean,
    pageCount: Int = 20,
    loadMoreEnd: Boolean = false,
    hasMore: Boolean = true,
    adapterState: AdapterUtils.AdapterState? = null
) {
    val size = data.size
    if (isFirst) {
        adapter.setNewInstance(null)
        if (size > 0) {
            adapter.setList(data)
            if (size < pageCount || !hasMore) {
                adapter.loadMoreModule.loadMoreFail()
                checkFooter(recyclerView, adapter, pageCount, loadMoreEnd)
                handleState(adapterState, AdapterUtils.AdapterStatus.FIRST_END)
            }
            handleState(adapterState, AdapterUtils.AdapterStatus.FIRST_INIT)
            return
        }
        adapter.loadMoreModule.loadMoreEnd()
        handleState(adapterState, AdapterUtils.AdapterStatus.FITST_EMPTY)
    } else {
        if (size > 0) {
            adapter.addData(data)
            handleState(adapterState, AdapterUtils.AdapterStatus.LOAD_MORE_ADD)
        }
        if (size < pageCount) {
            //加载到底了
            adapter.loadMoreModule.loadMoreEnd(loadMoreEnd)
            handleState(adapterState, AdapterUtils.AdapterStatus.LOAD_MORE_END)
        } else {
            //加载完成--取消加载样式
            if (hasMore) {
                adapter.loadMoreModule.loadMoreComplete()
                handleState(adapterState, AdapterUtils.AdapterStatus.LOAD_MORE_COMPLETE)
            } else {
                adapter.loadMoreModule.loadMoreEnd(loadMoreEnd)
                handleState(adapterState, AdapterUtils.AdapterStatus.LOAD_MORE_END)
            }
        }
    }
}

private fun <T, K : BaseViewHolder> checkFooter(
    recyclerView: RecyclerView,
    adapter: BaseQuickAdapter<T, K>,
    pageCount: Int,
    loadMoreEnd: Boolean
) {
    if (loadMoreEnd) return
    recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            if (adapter.data.size < pageCount) {
                adapter.loadMoreModule.loadMoreEnd(
                    !(recyclerView.canScrollVertically(1) || recyclerView.canScrollVertically(
                        -1
                    ))
                )
            }
        }
    })
}

private fun handleState(
    adapterState: AdapterUtils.AdapterState?,
    @AdapterUtils.AdapterStatus state: Int
) {
    adapterState?.adapterState(state)
}
