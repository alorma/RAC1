package com.alorma.rac1.ui.program

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramSection
import com.alorma.rac1.ui.common.adapterInflate
import kotlinx.android.synthetic.main.sections_row.view.*

class SectionsAdapter : RecyclerView.Adapter<SectionsAdapter.Holder>() {

    var sectionClick: ((ProgramSection) -> Unit)? = null
    lateinit var sessionBuilder: () -> SessionAdapter

    private var items: MutableMap<ProgramSection, List<SessionDto>> = mutableMapOf()
    private var openSections: Set<String> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(parent.adapterInflate(R.layout.sections_row))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val section = items.keys.toList()[position]
        holder.bind(section, items[section] ?: listOf(), sectionClick, sessionBuilder, openSections)
    }

    override fun getItemCount(): Int = items.keys.toList().size

    fun setItems(it: Map<ProgramSection, List<SessionDto>>) {
        this.items = it.toMutableMap()
        notifyDataSetChanged()
    }

    fun updateItem(section: String, sessions: List<SessionDto>, openSections: Set<String>) {
        this.openSections = openSections
        val set = items.keys
        val key = set.firstOrNull { it.id == section }
        key?.let {
            val position = set.toList().indexOf(it)
            items[it] = sessions
            notifyItemChanged(position)
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(section: ProgramSection,
                 list: List<SessionDto>,
                 sectionClick: ((ProgramSection) -> Unit)?,
                 sessionBuilder: () -> SessionAdapter,
                 openSections: Set<String>) {

            itemView.title.text = "${section.title} [${list.size}]"

            with(itemView.recyclerView) {
                layoutManager = LinearLayoutManager(context)
                adapter = sessionBuilder.invoke().apply {
                    addAll(list)
                }
                visibility = if (openSections.contains(section.id)) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            itemView.setOnClickListener {
                sectionClick?.invoke(section)
            }
        }
    }
}