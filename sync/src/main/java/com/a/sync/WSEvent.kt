package com.a.sync

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/11/17-17:11
 * 描    述：事件对象
 * 修订历史：
 * ================================================
 */
data class WSEvent(
    val from: WSMode,
    val eventType: WSEType,
    val commParams: Map<String, String>?,
    val viewC12c: ViewC12c?,
    val isIntercept: Boolean = false,
    val customParams: Map<String, String>? = null
)