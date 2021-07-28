package com.a.sync.server

import android.net.Uri
import android.os.Build
import android.util.Log
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
import java.net.URLEncoder


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
        get("/local_video") {
            val u = Uri.parse(this.call.request.uri)
            val videoName = u.getQueryParameter("name")
            Log.e(TAG, "url: name:" + videoName)
            if (videoName?.endsWith("m3u8") == true) {
                val file = File(videoName)
                val fileContent = file.readText(Charsets.UTF_8)
                val newFileContent = fileContent.replace("file://", Utils.MC_HTTP_VIDEO)
                    .replace("视频", URLEncoder.encode("视频"))
                val file2 = File(ContextUtils.applicationContext.cacheDir.absolutePath + "/a.m3u8")
                file2.writeText(newFileContent, Charsets.UTF_8)

                Log.e(TAG, newFileContent)
                call.respondFile(file2)
            } else {
                call.respondFile(File(videoName))
            }
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






