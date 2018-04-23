package com.alorma.rac1.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.alorma.rac1.R
import com.alorma.rac1.data.schedule.*

class ScheduleDays @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                             defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.days, this)
    }

    fun setSchedule(schedule: Schedule) {
        setupDay(Monday, schedule, findViewById(R.id.dayMonday))
        setupDay(Tuesday, schedule, findViewById(R.id.dayTuesday))
        setupDay(Wednesday, schedule, findViewById(R.id.dayWednesday))
        setupDay(Thursday, schedule, findViewById(R.id.dayThursday))
        setupDay(Friday, schedule, findViewById(R.id.dayFriday))
        setupDay(Saturday, schedule, findViewById(R.id.daySaturday))
        setupDay(Sunday, schedule, findViewById(R.id.daySunday))
    }

    private fun setupDay(day: ScheduleDay, schedule: Schedule, textView: TextView?) {
        textView?.isEnabled = day in schedule
    }

}