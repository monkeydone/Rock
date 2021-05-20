package com.bn.utils

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

fun String.toast() {
    globalApplicationContext()?.let {
        Toast.makeText(it.applicationContext, this, Toast.LENGTH_SHORT).show()
    }
}

fun dpToPx(dp: Float): Float {
    return dp * ContextUtils.applicationContext.getResources().getDisplayMetrics().density
}


fun globalApplicationContext(): Application? {
    var application: Application? = null
    val activityThreadClass: Class<*>
    try {
        activityThreadClass = Class.forName("android.app.ActivityThread")
        val method2: Method = activityThreadClass.getMethod(
                "currentActivityThread", *arrayOfNulls(0)
        )
        // 得到当前的ActivityThread对象
        val localObject: Any = method2.invoke(null, null as Array<Any?>?)
        val method: Method = activityThreadClass
            .getMethod("getApplication")
        application = method.invoke(localObject, null as Array<Any?>?) as Application
        return application
    } catch (e: ClassNotFoundException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    } catch (e: NoSuchMethodException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    } catch (e: IllegalArgumentException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }

    return null;

}

fun Int.toArtString(): String {
    return ContextUtils.applicationContext.getString(this)
}

fun Int.toArtString(vararg args: Any?) = ContextUtils.applicationContext.getString(this, *args)

fun Int.toArtDrawable() = ContextCompat.getDrawable(ContextUtils.applicationContext, this)

fun Int.toArtColor() = ContextCompat.getColor(ContextUtils.applicationContext, this)

