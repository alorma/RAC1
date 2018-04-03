package com.alorma.rac1.ui.program

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramSection
import com.alorma.rac1.ui.common.adapterInflate

class SectionsAdapter(private val sectionClick: (ProgramSection) -> Unit) : RecyclerView.Adapter<SectionsAdapter.Holder>() {

    private var items: MutableMap<ProgramSection, List<SessionDto>> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(parent.adapterInflate(android.R.layout.simple_list_item_1))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val section = items.keys.toList()[position]
        holder.bind(section, items[section] ?: listOf(), sectionClick)
    }

    override fun getItemCount(): Int = items.keys.toList().size

    fun setItems(it: Map<ProgramSection, List<SessionDto>>) {
        this.items = it.toMutableMap()
        notifyDataSetChanged()
    }

    fun updateItem(section: String, sessions: List<SessionDto>) {
        val key = items.keys.firstOrNull { it.id == section }
        key?.let {
            items[it] = sessions
            notifyDataSetChanged()
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(section: ProgramSection, list: List<SessionDto>, sectionClick: (ProgramSection) -> Unit) {
            itemView.findViewById<TextView>(android.R.id.text1).text = "${section.title} [${list.size}]"
            itemView.setOnClickListener {
                sectionClick(section)
            }
        }
    }
}