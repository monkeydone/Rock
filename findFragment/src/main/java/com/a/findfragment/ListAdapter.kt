package  com.a.findfragment

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.a.base.CommonMultiItem
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ListAdapter(data: List<CommonMultiItem<ListViewModel.ListDataModel>>) :
    BaseMultiItemQuickAdapter<CommonMultiItem<ListViewModel.ListDataModel>, ListAdapter.DataBindingViewHolder>(
        data
    ) {


    init {
        addItemType(CommonMultiItem.ITEM_HEADER, R.layout.fragment_header_list)
        addItemType(CommonMultiItem.ITEM_ONE, R.layout.fragment_item_list)
    }

    override fun convert(
        helper: DataBindingViewHolder,
        item: CommonMultiItem<ListViewModel.ListDataModel>
    ) {
        val binding = helper.binding
        binding.setVariable(BR.item, item.content)
        binding.executePendingBindings()


        when (helper.itemViewType) {
            CommonMultiItem.ITEM_HEADER -> helper.setText(R.id.iv_header, item.content?.letter)
            CommonMultiItem.ITEM_ONE -> when (helper.layoutPosition % 2) {
                0 -> helper.setText(R.id.tv_letter, "${item.content?.letter}")
                1 -> helper.setText(R.id.tv_letter, "${item.content?.fragmentObject?.showName}")
            }
        }
        binding.root.setOnClickListener {
//            item.content?.fragmentObject?.fragmentName?.toast()
            item.content?.fragmentObject?.let {
                ListActivity.startFragment(binding.root.context, it.fragmentName, it.showName)
            }
        }
    }


    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false)
                ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    inner class DataBindingViewHolder(view: View) : BaseViewHolder(view) {

        val binding: ViewDataBinding
            get() = itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }


}

