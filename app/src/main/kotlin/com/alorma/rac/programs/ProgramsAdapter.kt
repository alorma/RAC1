package com.alorma.rac.programs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.alorma.rac.R
import com.alorma.rac.data.api.Program
import com.alorma.rac.extension.onClick
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_program.*

class ProgramsAdapter : RecyclerView.Adapter<ProgramsAdapter.ProgramHolder>() {

    var items: List<Program> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_program, parent, false)
        return ProgramHolder(view)
    }

    override fun onBindViewHolder(holder: ProgramHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ProgramHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(program: Program) {
            programImage.load(program.images.personSmall)
            programText.text = program.title
            itemView.onClick {  }
        }
    }


}