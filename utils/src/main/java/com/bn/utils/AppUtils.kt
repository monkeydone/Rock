package com.bn.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File

object AppUtils {
    fun installApk(activity: Context, file: File?) {
        val intent = Intent()
        // 执行动作
        intent.action = Intent.ACTION_VIEW
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri =
                FileProvider.getUriForFile(activity, activity.packageName + ".fileProvider", file!!)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
//            checkoutInstallPermission(activity)
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        // activity任务栈中Activity的个数>0
        if (activity.getPackageManager().queryIntentActivities(intent, 0).size > 0) {
            activity.startActivity(intent)
        }

    }

    fun checkoutInstallPermission(activity: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val hasInstallPermission: Boolean =
                activity.getPackageManager().canRequestPackageInstalls()
            if (!hasInstallPermission) {
                startInstallPermissionSettingActivity(activity)
                return
            }
        }
    }

    private fun startInstallPermissionSettingActivity(activity: Context) {
        //注意这个是8.0新API
        val packageURI = Uri.parse("package:" + activity.getPackageName())
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (activity is Activity) {
            activity.startActivity(intent)
        }

    }

}