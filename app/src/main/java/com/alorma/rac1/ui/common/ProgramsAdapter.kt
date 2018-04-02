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
import org.threeten.bp.format.DateTimeFormatter
import java.time.LocalDateTime

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
            itemView.programSubtitle.text = item.times?.let { getSubtitleWithTime(item, it) } ?: item.subtitle
            itemView.programDescription.text = item.description

            itemView.setOnClickListener { onClick(item) }

            item.schedule?.let { setupDays(itemView, it) } ?: hideDays(itemView)
        }

        private fun getSubtitleWithTime(item: ProgramItem, it: Times): String {
            val startTime = it.start.format(DateTimeFormatter.ofPattern("HH:mm"))

            return if (it.duration.toHours() > 0) {
                if (it.duration.asMinutes() > 0) {
                    "${item.subtitle} - $startTime ${it.duration.toHours()}h ${it.duration.asMinutes()}m"
                } else {
                    "${item.subtitle} - $startTime ${it.duration.toHours()}h"
                }
            } else {
                "${item.subtitle} - $startTime ${it.duration.asMinutes()}m"
            }
        }

        private fun hideDays(itemView: View) {
            itemView.days.visibility = View.INVISIBLE
        }

        private fun setupDays(itemView: View, schedule: Schedule) {
            itemView.days.visibility = View.VISIBLE

            setupDay(Monday, schedule, itemView.dayMonday)
            setupDay(Tuesday, schedule, itemView.dayTuesday)
            setupDay(Wednesday, schedule, itemView.dayWednesday)
            setupDay(Thursday, schedule, itemView.dayThursday)
            setupDay(Friday, schedule, itemView.dayFriday)
            setupDay(Saturday, schedule, itemView.daySaturday)
            setupDay(Sunday, schedule, itemView.daySunday)
        }

        private fun setupDay(day: ScheduleDay, schedule: Schedule, textView: TextView) {
            textView.isEnabled = day in schedule
        }
    }
}