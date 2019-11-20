package com.alorma.rac.extension

import android.view.View

fun View.onClick(block: () -> Unit) {
    setOnClickListener { block() }
}
