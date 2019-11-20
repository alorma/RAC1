package com.alorma.rac.extension

import android.content.Context
import android.util.TypedValue

fun Context.getColorAttribute(colorAttr: Int): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(colorAttr, outValue, true)
    return outValue.data
}
