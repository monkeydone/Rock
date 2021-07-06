package com.a.base.list

import android.graphics.Rect
import android.view.View

/**
 * Created by su on 19-5-21.
 */
class SimpleGridDecoration(private val spacing: Int) :
    androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: androidx.recyclerview.widget.RecyclerView,
        state: androidx.recyclerview.widget.RecyclerView.State
    ) {
        outRect.set(spacing, spacing, spacing, spacing)
    }
}
