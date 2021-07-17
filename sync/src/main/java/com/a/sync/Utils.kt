package com.a.sync

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import androidx.annotation.RequiresPermission
import com.a.sync.server.HostInfo
import com.bn.utils.ContextUtils
import java.io.*


object Utils {


    @RequiresPermission(permission.ACCESS_WIFI_STATE)
    fun getIpAddressByWifi(): String {
        @SuppressLint("WifiManagerLeak") val wm =
            ContextUtils.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                ?: return ""
        return Formatter.formatIpAddress(wm.dhcpInfo.ipAddress)
    }


    /**
     * Wifi IP 地址
     */
    val IP_ADDRESS_BY_WIFI: String
        get() {
            try {
                return getIpAddressByWifi()
            } catch (e: Exception) {
                return "0.0.0.0"
            }
        }

    var MC_WS_PORT = 4444
    var MC_HTTP_PORT = 8000

    var WS_MODE: WSMode = WSMode.UNKNOW


    var HOST_INFO: HostInfo? = null


    /**
     * Return the manufacturer of the product/hardware.
     *
     * e.g. Xiaomi
     *
     * @return the manufacturer of the product/hardware
     */
    fun getManufacturer(): String? {
        return Build.MANUFACTURER
    }

    /**
     * Return the model of device.
     *
     * e.g. MI2SC
     *
     * @return the model of device
     */
    fun getModel(): String? {
        var model = Build.MODEL
        model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
        return model
    }

    fun getAppScreenWidth(): Int {
        return ContextUtils.applicationContext.resources.displayMetrics.widthPixels
    }

    fun getAppScreenHeight(): Int {
        return ContextUtils.applicationContext.resources.displayMetrics.heightPixels
    }

    @Throws(IOException::class)
    fun getResource(id: Int, context: Context): ByteArray? {
        val resources: Resources = context.resources
        val `is`: InputStream = resources.openRawResource(id)
        val bout = ByteArrayOutputStream()
        val readBuffer = ByteArray(4 * 1024)
        return try {
            var read: Int
            do {
                read = `is`.read(readBuffer, 0, readBuffer.size)
                if (read == -1) {
                    break
                }
                bout.write(readBuffer, 0, read)
            } while (true)
            bout.toByteArray()
        } finally {
            `is`.close()
        }
    }


    fun copyAssets(context: Context, filename: String, destName: String): String? {
        val assetManager: AssetManager = context.getAssets()
        var `in`: InputStream? = null
        var out: OutputStream? = null
        var outputFilePath: String? = null
        try {
            `in` = assetManager.open(filename)
            val outFile = File(context.cacheDir.absolutePath, destName)
            outputFilePath = outFile.absolutePath
            out = FileOutputStream(outFile)
            copyFile(`in`, out)
        } catch (e: IOException) {
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    // NOOP
                }
            }
        }
        return outputFilePath

    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream, out: OutputStream?) {
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            out?.write(buffer, 0, read)
        }
    }
}