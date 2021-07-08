package com.bn.pd.ui;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bn.pd.R;
import com.lxj.xpopup.impl.PartShadowPopupView;

/**
 * Description: 自定义局部阴影弹窗
 * Create by dance, at 2018/12/21
 */
public class CustomPartShadowPopupView2 extends PartShadowPopupView {
    public CustomPartShadowPopupView2(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_part_shadow_popup2;
    }

}
