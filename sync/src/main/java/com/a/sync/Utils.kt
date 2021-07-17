package com.a.sync

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import androidx.annotation.RequiresPermission
import com.a.sync.server.HostInfo
import com.bn.utils.ContextUtils

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

}