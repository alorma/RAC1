package com.alorma.rac.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeNotNull(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    observe(lifecycleOwner, Observer { it?.let(block) })
}