package com.alorma.rac.programs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alorma.rac.data.api.Program

class ProgramsAdapter : RecyclerView.Adapter<ProgramsAdapter.ProgramHolder>() {

    var items: List<Program> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        return ProgramHolder(view)
    }

    override fun onBindViewHolder(holder: ProgramHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


    class ProgramHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(program: Program) {
            itemView.findViewById<TextView>(android.R.id.text1).apply {
                text = program.title
                contentDescription = program.title
            }
        }
    }


}