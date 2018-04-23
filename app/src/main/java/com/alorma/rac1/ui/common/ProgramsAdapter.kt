package com.alorma.rac1.ui.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alorma.rac1.R
import com.alorma.rac1.data.schedule.*
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.Times
import com.alorma.rac1.domain.asMinutes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.program_row.view.*
import org.threeten.bp.Duration
import org.threeten.bp.format.DateTimeFormatter

class ProgramsAdapter(private val onClick: (ProgramItem) -> Unit)
    : RecyclerView.Adapter<ProgramsAdapter.Holder>() {

    private var items: List<ProgramItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(parent.adapterInflate(R.layout.program_row))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<ProgramItem>) {
        this.items = items
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ProgramItem, onClick: (ProgramItem) -> Unit) {
            itemView.programName.text = null
            itemView.programSubtitle.text = null
            itemView.programDescription.text = null

            val requestOptions = RequestOptions().transform(CircleCrop())
            Glide.with(itemView.context)
                    .load(item.images.person)
                    .apply(requestOptions)
                    .into(itemView.person)

            itemView.programName.text = item.title
            itemView.programSubtitle.text = item.subtitle
            item.times?.let {
                itemView.timeStart.visibility = View.VISIBLE
                itemView.timeStart.text = getSubtitleWithTime(it)
            } ?: hideTimeStart(itemView)
            itemView.programDescription.text = item.description

            itemView.setOnClickListener { onClick(item) }

            item.times?.let {
                itemView.timeDuration.visibility = View.VISIBLE
                itemView.timeDuration.text = duration(it.duration)
            } ?: hideDuration(itemView)

            item.schedule?.let { setupDays(itemView, it) } ?: hideDays(itemView)
        }

        private fun hideTimeStart(itemView: View) {
            itemView.timeStart.visibility = View.INVISIBLE
        }

        private fun hideDuration(itemView: View) {
            itemView.timeDuration.visibility = View.INVISIBLE
        }

        private fun getSubtitleWithTime(it: Times): String {
            return it.start.format(DateTimeFormatter.ofPattern("HH:mm"))
        }

        private fun duration(it: Duration): String {
            return if (it.toHours() > 0) {
                if (it.asMinutes() > 0) {
                    "${it.toHours()}:${it.asMinutes()}h"
                } else {
                    "${it.toHours()}h"
                }
            } else {
                "${it.asMinutes()}m"
            }
        }

        private fun hideDays(itemView: View) {
            itemView.days.visibility = View.INVISIBLE
        }

        private fun setupDays(itemView: View, schedule: Schedule) {
            itemView.days.visibility = View.VISIBLE
            itemView.days.setSchedule(schedule)
        }
    }
}