package com.alorma.rac1.ui.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.data.net.ProgramDto
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.program_row.view.*

class ProgramsAdapter(private val onClick: (ProgramDto) -> Unit)
    : RecyclerView.Adapter<ProgramsAdapter.Holder>() {

    private var items: List<ProgramDto> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(parent.adapterInflate(R.layout.program_row))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<ProgramDto>) {
        this.items = items
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(programDto: ProgramDto, onClick: (ProgramDto) -> Unit) {
            itemView.programName.text = null
            itemView.programSubtitle.text = null
            itemView.programSchedule.text = null
            itemView.programDescription.text = null

            val requestOptions = RequestOptions().transform(CircleCrop())
            Glide.with(itemView.context)
                    .load(programDto.images.person)
                    .apply(requestOptions)
                    .into(itemView.person)

            itemView.programName.text = programDto.title
            itemView.programSubtitle.text = programDto.subtitle
            itemView.programSchedule.text = programDto.schedule?.replace(", ", "\n")
            itemView.programDescription.text = programDto.description

            itemView.setOnClickListener { onClick(programDto) }
        }
    }
}