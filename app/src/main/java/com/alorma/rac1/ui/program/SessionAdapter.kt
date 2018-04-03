package com.alorma.rac1.ui.program

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.ui.common.adapterInflate
import kotlinx.android.synthetic.main.session_row.view.*

class SessionAdapter : RecyclerView.Adapter<SessionAdapter.Holder>() {

    private val items: MutableList<SessionDto> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(parent.adapterInflate(R.layout.session_row))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addAll(items: List<SessionDto>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(sessionDto: SessionDto) {
            itemView.title.text = sessionDto.title
        }
    }
}