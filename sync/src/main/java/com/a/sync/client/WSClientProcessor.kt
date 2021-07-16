package com.a.sync.client

import android.app.Activity
import com.a.sync.GsonUtils
import com.a.sync.WSEType
import com.a.sync.WSEvent
import com.a.sync.WSMode
import com.a.sync.mvvm.viewmodel.SyncListViewModel
import com.a.sync.server.HostInfo
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
object WSClientProcessor {
    private var mHostInfo: HostInfo? = null
    private var mRatioX: Float? = null
    private var mRatioY: Float? = null


    /**
     * 处理来自主机的消息
     */
    fun process(wsEvent: WSEvent) {
        //LogHelper.i(TAG, "wsEvent===>$wsEvent")
        when (wsEvent.eventType) {
            WSEType.WSE_TEST -> {
//                ToastUtils.showShort(wsEvent.message)
//                wsEvent.commParams.toString().toast()
                SyncListViewModel.addMessage(wsEvent.commParams.toString(), WSMode.CLIENT)
            }

            /**
             * 切换到前台
             */
            WSEType.APP_ON_FOREGROUND -> {
                val activityName = wsEvent.commParams?.get("activityName")
                activityName?.let {
                    val clazz: Class<Activity> =
                        Class.forName(it) as Class<Activity>
//                    DoKitCommUtil.changeAppOnForeground(clazz)
                }

            }
            /**
             * 切换到后台
             */
            WSEType.APP_ON_BACKGROUND -> {
//                ActivityUtils.startHomeActivity()
            }

            WSEType.WSE_CONNECTED -> {

                mHostInfo = GsonUtils.fromJson<HostInfo>(
                    wsEvent.commParams?.get("hostInfo"),
                    HostInfo::class.java
                )
                mHostInfo?.let {
//                    mRatioX = ScreenUtils.getAppScreenHeight() / mHostInfo?.width!!
//                    mRatioY = ScreenUtils.getAppScreenHeight() / mHostInfo?.height!!
                    "已经连接到${it.deviceName}主机".toast()
                }
            }

            /**
             * 模拟返回事件
             */
            WSEType.ACTIVITY_BACK_PRESSED -> {
//                val topActivity = ActivityUtils.getTopActivity()
//                if (wsEvent.commParams?.get("activityName") == topActivity::class.java.canonicalName) {
//                    topActivity.onBackPressed()
//                }

            }

            WSEType.ACTIVITY_FINISH -> {
//                val topActivity = ActivityUtils.getTopActivity()
//                if (wsEvent.commParams?.get("activityName") == topActivity::class.java.canonicalName) {
//                    topActivity.finish()
//                }
            }

            WSEType.WSE_ACCESS_EVENT -> {
                wsEvent.commParams?.let {
//                    LogHelper.json(
//                        TAG,
//                        "ServerActivityName===${it["activityName"]}    ClientActivityName===>${ActivityUtils.getTopActivity()::class.java.canonicalName}"
//                    )
//                    if (it["activityName"] != ActivityUtils.getTopActivity()::class.java.canonicalName) {
//                        "当前测试和主机不处于同一个页面，请手动调整同步".toast()
//                        return
//                    }

                }

//                wsEvent.viewC12c?.let { viewC12c ->
//                    if (DoKitWindowManager.ROOT_VIEWS == null || viewC12c.viewRootImplIndex == -1) {
//                        ToastUtils.showShort("匹配控件失败，请手动操作")
//                        return
//                    }
//                    var viewRootImpl: ViewParent? = null
//                    DoKitWindowManager.ROOT_VIEWS?.let { rootViews ->
//                        viewRootImpl = rootViews[viewC12c.viewRootImplIndex]
//                    }
//                    viewRootImpl?.let {
//                        val decorView: ViewGroup =
//                            ReflectUtils.reflect(it).field("mView").get<View>() as ViewGroup
//                        val targetView: View? =
//                            ViewPathUtil.findViewByViewParentInfo(decorView, viewC12c.viewPaths)
//                        targetView?.let { target ->
//                            DoKitConstant.MC_INTERCEPT?.let { interceptor ->
//                                if (wsEvent.isIntercept) {
//                                    custom(interceptor, target, wsEvent.customParams)
//                                } else {
//                                    comm(viewC12c, target)
//                                }
//                            } ?: comm(viewC12c, target)
//
//
//                        } ?: ToastUtils.showShort("匹配控件失败，请手动操作")
//
//                    } ?: ToastUtils.showShort("无法确定当前控件所属窗口")
//
//                }
//                    ?: ToastUtils.showShort("无法获取手势控件信息")
            }
        }
    }


}


