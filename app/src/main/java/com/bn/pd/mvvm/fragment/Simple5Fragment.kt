package com.bn.pd.mvvm.fragment

import android.content.Context
import android.view.View
import com.bn.pd.R
import com.bn.utils.toast
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils


class Simple5Fragment(context: Context) : BottomPopupView(context),
    View.OnClickListener {
    override fun getImplLayoutId(): Int {
        return R.layout.fragment_simple5
    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * .85f).toInt()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                "message".toast()
            }

        }
    }


}
