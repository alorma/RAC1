package com.alorma.rac.extension

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Context.getColorAttribute(colorAttr: Int): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(colorAttr, outValue, true)
    return outValue.data
}
