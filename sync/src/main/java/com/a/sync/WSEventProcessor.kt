package com.a.sync

import com.a.sync.client.WSClientProcessor
import com.a.sync.server.WSServerProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/11/18-17:32
 * 描    述：
 * 修订历史：
 * ================================================
 */
object WSEventProcessor {
    suspend fun process(wsEvent: WSEvent) {
        try {
            withContext(Dispatchers.Main) {
                when (wsEvent.from) {
                    WSMode.HOST -> WSClientProcessor.process(wsEvent)
                    WSMode.CLIENT -> WSServerProcessor.process(wsEvent)
                    else -> {

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun handleVideo(file: File) {

    }
}