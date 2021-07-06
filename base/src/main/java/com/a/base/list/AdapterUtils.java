package com.a.base.list;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class AdapterUtils {

    public interface AdapterState {
        void adapterState(@AdapterStatus int state);
    }

    @IntDef({
            AdapterStatus.FIRST_INIT,
            AdapterStatus.FIRST_END,
            AdapterStatus.FITST_EMPTY,
            AdapterStatus.LOAD_MORE_ADD,
            AdapterStatus.LOAD_MORE_COMPLETE,
            AdapterStatus.LOAD_MORE_END,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface AdapterStatus {
        int FIRST_INIT = 0;               // 第一次加载数据--数据大于零
        int FIRST_END = 1;               // 第一次加载数据--数据小于一页数据
        int FITST_EMPTY = 2;               // 第一次加载数据--数据为空
        int LOAD_MORE_ADD = 3;                //  加载更多
        int LOAD_MORE_COMPLETE = 4;            //  加载更多--加载完成
        int LOAD_MORE_END = 5;                //  加载更多---加载结束
    }
}
