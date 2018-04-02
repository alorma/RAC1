package com.alorma.rac1.ui.common

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.adapterInflate(@LayoutRes layout: Int) = LayoutInflater.from(context).inflate(layout, this, false)