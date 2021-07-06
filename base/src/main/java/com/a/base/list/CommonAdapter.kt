package com.art.ui.adapter.recyclerview

import androidx.databinding.ViewDataBinding
import com.a.base.BR
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder


/**
 * 单类型---基础adapter
 *
 *
 * layoutResId Databinding item--对应变量名
 *
 * @param <T> 数据类型
 */
open class CommonAdapter<T>(layoutResId: Int, data: MutableList<T>? = null) :
    BaseQuickAdapter<T, BaseDataBindingHolder<ViewDataBinding>>(layoutResId, data), LoadMoreModule {

    private var bindingListener: ((ViewDataBinding, T, BaseDataBindingHolder<ViewDataBinding>) -> Unit)? =
        null

    fun setItemListener(e: (ViewDataBinding, T, BaseDataBindingHolder<ViewDataBinding>) -> Unit) {
        bindingListener = e
    }

    override fun convert(holder: BaseDataBindingHolder<ViewDataBinding>, item: T) {
        holder.dataBinding?.apply {
            setVariable(BR.item, item)
            setVariable(BR.position, holder.adapterPosition)
            setVariable(BR.adapter, this@CommonAdapter)
            bindingListener?.let { it(this, item, holder) }
            executePendingBindings()
        }
    }
}


fun <T> BaseQuickAdapter<T, BaseDataBindingHolder<ViewDataBinding>>.replace(
    position: Int,
    item: T
) {
    data[position] = item
    if (!recyclerView.isComputingLayout) {
        notifyItemChanged(position)
    } else {
        recyclerView.post { notifyItemChanged(position) }
    }
}

