package com.alorma.rac.tv.base

import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter

fun DetailsSupportFragment.createActionsAdapter(
    vararg actions: Pair<Long, CharSequence>,
    onActionSelected: (Action) -> Unit
): ArrayObjectAdapter {
    return ArrayObjectAdapter().apply {
        actions.forEach { add(Action(it.first, it.second)) }
        setOnItemViewClickedListener { itemViewHolder,
                                       item,
                                       rowViewHolder,
                                       row ->
            (item as? Action)?.let(onActionSelected)
        }
    }
}