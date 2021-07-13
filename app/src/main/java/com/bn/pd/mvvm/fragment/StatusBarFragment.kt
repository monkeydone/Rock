package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentStatusBarBinding
import com.bn.pd.mvvm.viewmodel.StatusBarViewModel
import com.bn.utils.loadImageByUrl
import com.bn.utils.toArtColor
import com.bn.utils.toast
import com.jaeger.library.StatusBarUtil


@FragmentAnnotation("StatusBar", "Demo")
class StatusBarFragment : RBaseFragment<StatusBarViewModel, FragmentStatusBarBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_status_bar

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
        val url = "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg"
        binding.ivImage.loadImageByUrl(url)
    }

    override fun initData() {
        viewModel.loadData()

    }

    var alpha = 10
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                "message".toast()
                StatusBarUtil.setColor(
                    requireActivity(),
                    R.color.permissionx_text_color.toArtColor()
                );
            }
            R.id.tv_event -> {
                alpha = (alpha + 10) % 100
                StatusBarUtil.setTranslucent(requireActivity(), alpha)
                "配置透明度 ${alpha}".toast()
            }
            R.id.tv_transparent -> {
                "背景透明"
                StatusBarUtil.setTransparent(requireActivity())
            }
            R.id.tv_dark_mode -> {
                StatusBarUtil.setDarkMode(requireActivity())
            }
            R.id.tv_light_mode -> {
                StatusBarUtil.setLightMode(requireActivity())
            }

        }
    }


}
