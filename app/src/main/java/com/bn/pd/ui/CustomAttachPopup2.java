package com.bn.pd.ui;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bn.pd.R;
import com.lxj.xpopup.core.AttachPopupView;

/**
 * Description: 自定义背景的Attach弹窗
 * Create by lxj, at 2019/3/13
 */
public class CustomAttachPopup2 extends AttachPopupView {
    public CustomAttachPopup2(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_attach_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

    }
}
