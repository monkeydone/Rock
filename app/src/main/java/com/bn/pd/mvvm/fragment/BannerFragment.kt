package com.bn.pd.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentBannerBinding
import com.bn.pd.mvvm.adapter.ImageNetAdapter
import com.bn.pd.mvvm.viewmodel.BannerViewModel
import com.bn.utils.toast
import com.youth.banner.indicator.CircleIndicator


@FragmentAnnotation("Banner", "Demo")
class BannerFragment : RBaseFragment<BannerViewModel, FragmentBannerBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_banner

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel

    }

    override fun initData() {
        viewModel.loadData()

        //自定义的图片适配器，也可以使用默认的BannerImageAdapter
        val adapter = ImageNetAdapter(viewModel.getTestData3())

        binding.banner.setAdapter(adapter) //              .setCurrentItem(0,false)
            .addBannerLifecycleObserver(this) //添加生命周期观察者
            .setIndicator(CircleIndicator(requireContext())) //设置指示器


        binding.banner2.setAdapter(adapter) //              .setCurrentItem(0,false)
            .addBannerLifecycleObserver(this) //添加生命周期观察者
            .setIndicator(CircleIndicator(requireContext())) //设置指示器
            .setBannerGalleryEffect(40, 10)
            .setIndicator(CircleIndicator(requireContext()))
//            .setBannerGalleryMZ(20)
        binding.banner3.setAdapter(adapter) //              .setCurrentItem(0,false)
            .addBannerLifecycleObserver(this) //添加生命周期观察者
            .setIndicator(CircleIndicator(requireContext()))
            .setBannerGalleryMZ(20)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                "message".toast()
            }

        }
    }


}
