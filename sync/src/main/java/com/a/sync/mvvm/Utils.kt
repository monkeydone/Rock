package com.a.sync.mvvm

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import androidx.annotation.RequiresPermission
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


}