package com.alorma.rac.extension

inline operator fun <reified T> T.plus(list: List<T>): List<T> = listOf(this, *list.toTypedArray())