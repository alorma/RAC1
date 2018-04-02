package com.alorma.rac1.ui.common

import android.support.v7.util.DiffUtil

@DslMarker
annotation class DiffUtilsDsl

@DiffUtilsDsl
class DiffUtilsBuilder<R> {

    lateinit var comparable: Comparator<R>
    var oldList: MutableList<R> = mutableListOf()
    var newList: List<R> = listOf()

    fun build(): DiffUtil.DiffResult =
            AdapterDiff(oldList, newList, comparable)
                    .run { DiffUtil.calculateDiff(this) }
                    .also<DiffUtil.DiffResult> {
                        oldList.apply {
                            clear()
                            addAll(newList)
                        }
                    }
}

class AdapterDiff<R>(private val oldList: List<R>,
                     private val newList: List<R>,
                     private val identityComparator: Comparator<R>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = identityComparator.compare(oldList[oldItemPosition], newList[newItemPosition]) == 0

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
}

inline fun <reified R> MutableList<R>.diffDSL(setup: DiffUtilsBuilder<R>.() -> Unit):
        DiffUtil.DiffResult {
    return with(DiffUtilsBuilder<R>()) {
        oldList = this@diffDSL
        setup()
        build()
    }
}