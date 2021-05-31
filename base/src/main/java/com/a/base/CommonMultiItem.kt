package com.a.base

import com.chad.library.adapter.base.entity.MultiItemEntity

class CommonMultiItem<T> : MultiItemEntity {
    private var itemType: Int = 0
    var spanSize: Int = 0

    constructor(itemType: Int, spanSize: Int, content: T) {
        this.itemType = itemType
        this.spanSize = spanSize
        this.content = content
    }

    constructor(itemType: Int, spanSize: Int) {
        this.itemType = itemType
        this.spanSize = spanSize
    }

    var content: T? = null

    override fun getItemType(): Int {
        return itemType
    }

    companion object {
        const val ITEM_HEADER = 1
        const val ITEM_ONE = 2
    }
}
