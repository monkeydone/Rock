package com.a.sync.server

import com.a.sync.GsonUtils
import com.a.sync.Utils
import com.a.sync.WSEvent
import com.a.sync.WSMode
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/11/13-15:38
 * 描    述：
 * 修订历史：
 * ================================================
 */
object DoKitWsServer {
    /**
     * 所有的连接
     */
    internal val wsSessionMaps: MutableMap<String?, DefaultWebSocketServerSession> = mutableMapOf()

    private val server: CIOApplicationEngine by lazy {
        embeddedServer(
            CIO,
            port = Utils.MC_WS_PORT,
            host = Utils.IP_ADDRESS_BY_WIFI,
            module = WSRouter
        )
    }

    private val httpServer: NettyApplicationEngine by lazy {
        embeddedServer(Netty, port = Utils.MC_HTTP_PORT, host = Utils.IP_ADDRESS_BY_WIFI) {
            routing {
                get("/") {
                    call.respondText("Hello, world!")
                }
                get("/demo") {
                    call.respondText("HELLO WORLD!")
                }
            }
        }
    }
    //val engine

    fun start(callBack: () -> Unit) {
        try {
            server.start()
            httpServer.start()
            Utils.WS_MODE = WSMode.HOST
            callBack()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun stop() {
        try {
            server.stop(1, 1)
            httpServer.stop(1, 1)
            Utils.WS_MODE = WSMode.UNKNOW
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun send(wsEvent: WSEvent) {
        wsSessionMaps.forEach {
            CoroutineScope(it.value.coroutineContext).launch {
                if (it.value.isActive) {
                    it.value.outgoing.send(Frame.Text(GsonUtils.toJson(wsEvent)))
                }
            }
        }
    }
}