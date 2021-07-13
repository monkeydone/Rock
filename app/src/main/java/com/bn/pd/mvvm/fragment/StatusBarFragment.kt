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
import java.util.*


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
                alpha = (alpha + 10) % 255
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
            R.id.tv_set_top_imageview -> {
                StatusBarUtil.setTranslucentForImageView(requireActivity(), binding.ivImage);
            }
            R.id.tv_set_banner_color -> {
                val random = Random()
                val mColor = 0x000000 or random.nextInt(0xffffff)
                alpha = (alpha + 10) % 255
                StatusBarUtil.setColor(requireActivity(), mColor, alpha)
                "color ${mColor} alpha ${alpha} ".toast()
            }

        }
    }


}
