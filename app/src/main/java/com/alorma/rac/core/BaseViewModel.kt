package com.alorma.rac.core

import androidx.lifecycle.*

open class BaseViewModel : ViewModel(), LifecycleObserver {

    var lifecycle: LifecycleOwner? = null
        set(value) {
            value?.lifecycle?.addObserver(this)
            field = value
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {

    }

}