package com.a.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import java.lang.reflect.ParameterizedType

object ClassUtil {
    /**
     * 获取泛型ViewModel的class对象
     */
    fun <T> getViewModel(obj: Any): Class<T>? {
        val currentClass: Class<*> = obj.javaClass
        val tClass: Class<T>? = getGenericClass(currentClass)
        return if (tClass == null || tClass == NoViewModel::class.java) {
            null
        } else tClass
    }

    private fun <T> getGenericClass(klass: Class<*>): Class<T>? {
        val type = klass.genericSuperclass as? ParameterizedType ?: return null
        val types = type.actualTypeArguments
        for (t in types) {
            val tClass = t as Class<T>
            if (AndroidViewModel::class.java.isAssignableFrom(tClass)) {
                return tClass
            }
        }
        return null
    }
}

class NoViewModel(app: Application) : BaseViewModel<Int>(app)