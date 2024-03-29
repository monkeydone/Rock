package com.bn.pd.mvvm.fragment

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.a.base.RBaseFragment
import com.a.base.funOwnerObserver
import com.a.base.list.SimpleGridDecoration
import com.a.base.list.loadListData
import com.a.base.observer
import com.a.findfragment.FragmentAnnotation
import com.art.ui.adapter.recyclerview.CommonAdapter
import com.bn.pd.R
import com.bn.pd.databinding.FragmentItemList1Binding
import com.bn.pd.databinding.FragmentList1Binding
import com.bn.pd.mvvm.viewmodel.ImageListViewModel
import com.bn.pd.ui.CustomPartShadowPopupView
import com.bn.pd.ui.CustomPartShadowPopupView2
import com.bn.pd.ui.PagerDrawerPopup
import com.bn.utils.toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.interfaces.SimpleCallback
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File

@FragmentAnnotation("ImageList", "Demo")
class ImageListFragment : RBaseFragment<ImageListViewModel, FragmentList1Binding>(), OnItemClickListener,
        OnItemChildClickListener,
        OnItemLongClickListener,
        OnLoadMoreListener, View.OnClickListener {

    private val adapter =
            object : CommonAdapter<ImageListViewModel.List1DataModel>(R.layout.fragment_item_list1) {
                override fun convert(
                        holder: BaseDataBindingHolder<ViewDataBinding>,
                        item: ImageListViewModel.List1DataModel
                ) {
                    super.convert(holder, item)
                    val binding = holder.dataBinding as FragmentItemList1Binding
                    binding.tvLetter.setBackgroundColor(Color.CYAN)
                }
            }.apply {
                addChildClickViewIds(R.id.iv_image, R.id.tv_letter)
                setOnItemClickListener(this@ImageListFragment)
                setOnItemLongClickListener(this@ImageListFragment)
                setOnItemChildClickListener(this@ImageListFragment)
                loadMoreModule.setOnLoadMoreListener(this@ImageListFragment)
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
        binding.onClickListener = this
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
        val data = adapter.data[position] as ImageListViewModel.List1DataModel
        "${data.letter}  ${data.url}".toast()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = adapter.data[position] as ImageListViewModel.List1DataModel
        when (view.id) {
            R.id.iv_image -> {
//                data.url.toast()
                XPopup.Builder(requireContext())
                    .asImageViewer(
                        view as ImageView,
                        position,
                        viewModel.itemList.value?.map { it.url } as List<Object>,
                        true,
                        true,
                        -1,
                        -1,
                        -1,
                        true,
                        Color.rgb(32, 36, 46),
                        { popupView, position ->
//                            val rv = holder.itemView.getParent() as RecyclerView
//                            popupView.updateSrcView(rv.getChildAt(position) as ImageView)
                        },
                        object : XPopupImageLoader {
                            override fun loadImage(position: Int, uri: Any, imageView: ImageView) {

                                //如果你确定你的图片没有超级大的，直接这样写就行
                                Glide.with(imageView).load(uri.toString())
                                    .apply(RequestOptions().override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL))
                                    .into(imageView)
                            }

                            override fun getImageFile(context: Context, uri: Any): File? {
                                try {
                                    return Glide.with(context).downloadOnly().load(uri).submit()
                                        .get()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                return null
                            }

                        }, null
                    )
                    .show()
            }
            R.id.tv_letter -> {
                data.letter.toast()
            }
        }
    }

    override fun onItemLongClick(
        adapter: BaseQuickAdapter<*, *>,
        view: View,
        position: Int
    ): Boolean {

        val attachPopupView = XPopup.Builder(context)
            .hasShadowBg(false)
            .isClickThrough(true)
            .offsetX(80)
            .popupPosition(PopupPosition.Top) //手动指定弹窗的位置
            .atView(view) // 依附于所点击的View，内部会自动判断在上方或者下方显示
            .asAttachList(
                arrayOf(
                    "分享",
                    "编辑",
                    "分享",
                    "编辑",
                    "分享",
                    "编辑",
                    "分享",
                    "编辑",
                    "分享",
                    "编辑",
                    "不带icon不带icon",
                    "分享分享分享"
                ),
                null,  //                                new int[]{R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round},
                { position, text -> text.toast() }, 0, 0 /*, Gravity.LEFT*/
            )

        attachPopupView.show()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvCenter -> {
                XPopup.Builder(context)
                    .atView(v)
                    .popupPosition(PopupPosition.Top)
                    .asCustom(CustomPartShadowPopupView2(requireContext()))
                    .show()
            }
            R.id.tvCenter2 -> {
                XPopup.Builder(context)
                    .atView(v)
                    .popupPosition(PopupPosition.Bottom)
                    .asCustom(CustomPartShadowPopupView2(requireContext()))
                    .show()
            }
            R.id.tv_all -> {
                val popupView = XPopup.Builder(context)
                    .atView(v)
                    .isClickThrough(true) //                    .dismissOnTouchOutside(false)
                    //                    .isCenterHorizontal(true)
                    .autoOpenSoftInput(true) //                    .offsetY(-150)
                    //                    .offsetX(100)
                    //                .dismissOnTouchOutside(false)
                    .setPopupCallback(object : SimpleCallback() {
                        override fun onShow(popupView: BasePopupView) {
                            "显示了".toast()
                        }

                        override fun onDismiss(popupView: BasePopupView) {}
                    })
                    .asCustom(CustomPartShadowPopupView(requireContext())) as CustomPartShadowPopupView
                popupView.toggle()
            }
            R.id.tv_filter -> {
                XPopup.Builder(context)
                    .isDestroyOnDismiss(true)
                    .popupPosition(PopupPosition.Right) //右边
                    .hasStatusBarShadow(true) //启用状态栏阴影
                    .asCustom(PagerDrawerPopup(requireActivity()))
                    .toggle()
            }
            R.id.tv_select -> {
                XPopup.Builder(context)
                    .atView(v)
                    .asCustom(CustomPartShadowPopupView(requireContext()))
                    .show()
            }


        }
    }


}

