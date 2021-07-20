package com.a.sync.server

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.a.sync.R
import com.a.sync.Utils
import com.a.sync.mvvm.viewmodel.LocalVideoListViewModel
import com.bn.utils.ContextUtils
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File


/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/6/23-14:35
 * 描    述：
 * 修订历史：
 * ================================================
 */
@RequiresApi(Build.VERSION_CODES.O)
val HTTPRouter: Application.() -> Unit = {
    install(DefaultHeaders)
    install(CallLogging)

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        get("/demo") {
            call.respondText("HELLO WORLD!")
        }
        get("/m") {
            call.respondBytes(
                Utils.getResource(
                    R.raw.media,
                    ContextUtils.applicationContext
                )!!
            )
        }
        get("/m3") {
            val path2 =
                Utils.copyAssets(ContextUtils.applicationContext, "media.mp4", "m.mp4")
            call.respondFile(File(path2!!))
        }
        get("/m3u8") {
            val u = Uri.parse(this.call.request.uri)
            val videoName = u.getQueryParameter("name")
            val path2 =
                Utils.copyAssets(ContextUtils.applicationContext, "video.txt", "m.m3u8")
            call.respondFile(File(path2!!))
        }
        get("/video") {
            val u = Uri.parse(this.call.request.uri)
            val videoName = u.getQueryParameter("name")
            if (videoName != null) {
                var path =
                    Utils.getFileForFileName(videoName, LocalVideoListViewModel.fileList)
                if (path == null) {
                    path = File(videoName)
                }
                if (path == null || !path.exists()) {
                    path = File("/storage/emulated/0/QQBrowser/视频/$videoName")
                }

                call.respondFile(File(path?.absolutePath))
            }
//                    call.respondText(u.getQueryParameter("name")?:"error")


        }
    }
}






