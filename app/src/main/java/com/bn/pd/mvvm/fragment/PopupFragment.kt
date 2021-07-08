package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentPopupBinding
import com.bn.pd.mvvm.viewmodel.PopupViewModel
import com.bn.utils.toast
import com.lxj.xpopup.XPopup


@FragmentAnnotation("Popup", "Template")
class PopupFragment : RBaseFragment<PopupViewModel, FragmentPopupBinding>(), View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_popup

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
    }

    override fun initData() {
        viewModel.loadData()
    }

    fun toast(str: String) {
        str.toast()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.normal_dialog -> {
                val popupView =
                    XPopup.Builder(
                        context
                    ).isDestroyOnDismiss(true)
                        .asConfirm(
                            "哈哈", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                            "取消", "确定",
                            { "click confirm".toast() }, null, false
                        )
                popupView.show()
            }
            R.id.tv_custom_layout -> {
                val content = """
     您可以复用项目已有布局，来使用XPopup强大的交互能力和逻辑封装，弹窗的布局完全由你自己控制。
     注意：你自己的布局必须提供一些控件Id，否则XPopup找不到View。
     具体需要提供哪些Id，请查看文档[内置弹窗]一章。
     """.trimIndent()
                XPopup.Builder(context)
                    .autoOpenSoftInput(true)
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个

                    .asConfirm(
                        "复用项目已有布局", content,
                        "关闭", "XPopup牛逼",
                        { "click confirm".toast() }, null, false, R.layout.my_confim_popup
                    ) //最后一个参数绑定已有布局
                    .show()
            }
            R.id.tv_list -> {
                XPopup.Builder(context)
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .asCenterList(
                        "请选择一项", arrayOf("条目1", "条目2", "条目3", "条目4"),
                        null, 1
                    ) { position, text -> toast("click $text") }
                    .show()
            }
            R.id.tv_bottom_list -> {
                XPopup.Builder(context)
                    .isDarkTheme(true)
                    .hasShadowBg(true) //                            .hasBlurBg(true)
                    //                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .asBottomList(
                        "请选择一项", arrayOf("条目1", "条目2", "条目3", "条目4", "条目5", "条目6", "条目7"), null, 2
                    ) { position, text -> toast("click $text") }.show()
            }
            R.id.tv_custom_bottom_list -> {
                XPopup.Builder(context)
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                    .asCustom(Simple5Fragment(requireContext()))
                    .show()
            }

        }
    }


}
