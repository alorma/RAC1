package com.alorma.rac1.ui.common

import android.support.v7.widget.Toolbar
import com.alorma.rac1.R

@DslMarker
annotation class ToolbarDsl

@ToolbarDsl
class ItemBuilder {

    lateinit var action: () -> Unit
    var id: Int = 0

    fun build() = id to action
}

@ToolbarDsl
class BackBuilder {
    var action: (() -> Unit)? = null
    var icon: Int = R.drawable.ic_arrow

    fun setup(toolbar: Toolbar) {
        action?.let { backAction ->
            toolbar.setNavigationIcon(icon)
            toolbar.setNavigationOnClickListener {
                backAction()
            }
        }
    }
}


@ToolbarDsl
class ToolbarBuilder {

    lateinit var toolbar: Toolbar
    var back: BackBuilder? = null
    var title: Any? = null
    var subTitle: Any? = null
    var menu: Int = 0
    var items = mutableListOf<Pair<Int, () -> Unit>>()

    fun build(): Toolbar {

        when (title) {
            is Int -> {
                val titleInt = title as Int
                if (titleInt != 0) {
                    toolbar.title = toolbar.resources.getString(titleInt)
                }
            }
            is String -> {
                val titleString = title as String
                if (titleString.isEmpty().not()) {
                    toolbar.title = titleString
                }
            }
        }

        when (subTitle) {
            is Int -> {
                val subTitleInt = subTitle as Int
                if (subTitleInt != 0) {
                    toolbar.subtitle = toolbar.resources.getString(subTitleInt)
                }
            }
            is String -> {
                val subTitleString = subTitle as String
                if (subTitleString.isEmpty().not()) {
                    toolbar.subtitle = subTitleString
                }
            }
        }

        if (menu != 0) {
            toolbar.inflateMenu(menu)
        }

        back?.setup(toolbar)

        toolbar.setOnMenuItemClickListener {
            items.firstOrNull { item -> it.itemId == item.first }
                    ?.second
                    ?.invoke()
            true
        }

        return toolbar
    }

    fun item(id: Int = 0, setup: ItemBuilder.() -> Unit) {
        val builder = ItemBuilder()
        builder.id = id
        builder.setup()
        items.add(builder.build())
    }

    fun back(setup: BackBuilder.() -> Unit) {
        back = BackBuilder().apply(setup)
    }
}

@ToolbarDsl
fun toolbarDSL(setup: ToolbarBuilder.() -> Unit) {
    with(ToolbarBuilder()) {
        setup()
        build()
    }
}

fun Toolbar.dsl(setup: ToolbarBuilder.() -> Unit) {
    with(ToolbarBuilder()) {
        toolbar = this@dsl
        setup()
        build()
    }
}