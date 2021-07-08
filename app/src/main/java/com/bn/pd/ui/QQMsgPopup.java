package com.bn.pd.ui;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bn.pd.R;
import com.lxj.xpopup.core.PositionPopupView;

/**
 * Description: 自定义自由定位Position弹窗
 * Create by dance, at 2019/6/14
 */
public class QQMsgPopup extends PositionPopupView {
    public QQMsgPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_attach_popup;
    }
}
