package com.bn.pd.mvvm.fragment

import android.view.Gravity
import android.view.View
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentFloatViewBinding
import com.bn.pd.mvvm.viewmodel.FloatViewViewModel
import com.bn.utils.toast
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnInvokeView


@FragmentAnnotation("FloatView", "Demo")
class FloatViewFragment : RBaseFragment<FloatViewViewModel, FragmentFloatViewBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_float_view

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
    }

    override fun initData() {
        viewModel.loadData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
//                "message".toast()

                EasyFloat.with(requireActivity()).setLayout(R.layout.fragment_float_view).show()

            }
            R.id.tv_event -> {
                EasyFloat.with(requireActivity()).setLayout(R.layout.fragment_fullscreen)
                    .setMatchParent(
                        widthMatch = true,
                        heightMatch = true
                    ).show()
            }
            R.id.tv_fullscreen -> {
                EasyFloat.with(requireActivity()).setTag("fullscreen")
                    .setLayout(R.layout.fragment_fullscreen,
                        OnInvokeView {
                            val ivLogo = it.findViewById<View>(R.id.message)
                            val ivLogo2 = it.findViewById<View>(R.id.tv_event)
                            ivLogo.setOnClickListener {
                                "message".toast()
                            }
                            ivLogo2.setOnClickListener {
                                EasyFloat.dismiss("fullscreen")
                            }
                        })
                    .setMatchParent(
                        widthMatch = true,
                        heightMatch = true
                    )
                    .registerCallback {
                        touchEvent { view, motionEvent ->
                            "${view.toString()} ${motionEvent.action}".toast()
                        }
                    }
                    .show()
            }
            R.id.tv_view_event -> {
                EasyFloat.with(requireActivity())
                    .setTag(tag)
                    .setLayout(R.layout.fragment_fullscreen,
                        OnInvokeView {
                            val ivLogo = it.findViewById<View>(R.id.message)
                            val ivLogo2 = it.findViewById<View>(R.id.tv_event)
                            ivLogo.setOnClickListener {
                                ivLogo2.visibility =
                                    if (ivLogo2.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                                EasyFloat.updateFloat(tag)
                            }
                            ivLogo2.setOnClickListener {
                                ivLogo.visibility =
                                    if (ivLogo.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                                EasyFloat.updateFloat(tag)
                            }
                        })
                    .setBorder(
                        binding.viewBg.left,
                        binding.viewBg.top,
                        binding.viewBg.right,
                        binding.viewBg.bottom
                    )
                    .setGravity(Gravity.CENTER)
                    .setSidePattern(SidePattern.RESULT_SIDE)
                    .show()
            }

        }
    }


}
