package com.a.sync.server

import com.a.sync.WSEType
import com.a.sync.WSEvent
import com.a.sync.WSMode
import com.a.sync.mvvm.fragment.SyncSimpleFragment
import com.a.sync.mvvm.viewmodel.SyncListViewModel
import com.a.videoplayer.mvvm.fragment.VideoPlayerFragment
import com.bn.utils.toast

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/11/18-17:32
 * 描    述：
 * 修订历史：
 * ================================================
 */
object WSServerProcessor {

    /**
     * 处理来自从机的消息
     */
    suspend fun process(wsEvent: WSEvent) {
        when (wsEvent.eventType) {
            WSEType.WSE_TEST -> {
                wsEvent.commParams.toString().toast()
                SyncListViewModel.addMessage(wsEvent.commParams.toString(), WSMode.CLIENT)
                SyncListViewModel.liveRefreshData.postValue(true)

            }

            WSEType.WSE_VIDEO -> {
                wsEvent.commParams.toString().toast()
                val url = wsEvent.commParams?.get(WSEType.WSE_VIDEO.toString())
                if (url != null) {
                    VideoPlayerFragment.openVideo(url)
                }
            }

            WSEType.WSE_CONNECTED -> {
//                ToastUtils.showShort(wsEvent.message)
//                wsEvent.eventType.toString().toast()
                SyncSimpleFragment.liveConnectionInfo.value = wsEvent.eventType.toString()
            }

            WSEType.WSE_CLOSE -> {
                //ToastUtils.showShort(wsEvent.message)
            }

            WSEType.ACTIVITY_BACK_PRESSED -> {
                //ToastUtils.showShort(wsEvent.message)
                //ActivityUtils.getTopActivity().onBackPressed()
            }
        }
    }


}