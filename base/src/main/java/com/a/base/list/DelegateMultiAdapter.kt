package com.a.base.list

import androidx.databinding.ViewDataBinding
import com.a.base.BR
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder


/**
 *  数据类型单一  布局为多类型
 *
 *  setMultiTypeDelegate(new MyMultiTypeDelegate());  通过设置代理 来对布局进行分类
 *
 * @param <T> 数据类型
 */
class DelegateMultiAdapter<T> :
    BaseDelegateMultiAdapter<T, BaseDataBindingHolder<ViewDataBinding>>(), LoadMoreModule {

    private var bindingListener: ((ViewDataBinding, T, BaseDataBindingHolder<ViewDataBinding>) -> Unit)? =
        null

    override fun convert(holder: BaseDataBindingHolder<ViewDataBinding>, item: T) {
        holder.dataBinding?.apply {
            setVariable(BR.item, item)
            setVariable(BR.position, holder.adapterPosition)
            setVariable(BR.adapter, this@DelegateMultiAdapter)
            bindingListener?.let { it(this, item, holder) }
            executePendingBindings()
        }
    }

    fun setItemListener(e: (ViewDataBinding, T, BaseDataBindingHolder<ViewDataBinding>) -> Unit) {
        bindingListener = e
    }
}



