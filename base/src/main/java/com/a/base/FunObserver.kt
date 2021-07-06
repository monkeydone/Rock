package com.a.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class FunObserver<T>(
    owner: LifecycleOwner? = null,
    val liveData: LiveData<T>,
    val callBack: (T) -> Unit,
    val oneTime: Boolean
) : Observer<T> {

    init {
        if (owner == null) {
            liveData.observeForever(this)
        } else {
            liveData.observe(owner, this)
        }
    }

    override fun onChanged(t: T?) {
        t?.let { callBack.invoke(t) }
        if (oneTime) liveData.removeObserver(this)
    }

}

fun <T> LifecycleOwner.observer(liveData: LiveData<T>, callBack: (T) -> Unit) {
    FunObserver(this, liveData, callBack, false)
}

fun <T> LifecycleOwner.funOwnerObserver(
    liveData: LiveData<T>,
    oneTime: Boolean = true,
    callBack: (T) -> Unit
) {
    FunObserver(this, liveData, callBack, oneTime)
}

fun <T> funObserver(liveData: LiveData<T>, callBack: (T) -> Unit) {
    FunObserver(null, liveData, callBack, true)
}

fun <T> funObserver(owner: LifecycleOwner?, liveData: LiveData<T>, callBack: (T) -> Unit) {
    FunObserver(owner, liveData, callBack, true)
}

fun <T> funObserver(
    owner: LifecycleOwner?,
    liveData: LiveData<T>,
    oneTime: Boolean,
    callBack: (T) -> Unit
) {
    FunObserver(owner, liveData, callBack, oneTime)
}

