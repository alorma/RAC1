package com.alorma.rac1.ui.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.net.ProgramDto
import kotlinx.android.synthetic.main.program_row.view.*

class ProgramsAdapter : RecyclerView.Adapter<ProgramsAdapter.Holder>() {

    private val items = mutableListOf<ProgramDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(parent.adapterInflate(R.layout.program_row))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addAll(newItems: List<ProgramDto>) {
        diffDSL<ProgramDto> {
            oldList = items
            newList = newItems
            comparable = compareBy { it.id }
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(programDto: ProgramDto) {
            itemView.title.text = programDto.title
        }
    }
}