package com.alorma.rac1.ui.program

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.domain.ProgramItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.program_info_fragment.*

class ProgramInfoFragment : Fragment() {

    var programItem: ProgramItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.program_info_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        programItem?.let {
            onLoad(it)
        }
    }

    private fun onLoad(it: ProgramItem) {
        programTitle.text = it.title
        programSchedule.text = it.scheduleText
        programDescription.text = it.description

        programSchedule.visibility = if (it.scheduleText != null) {
            View.VISIBLE
        } else {
            View.GONE
        }

        Glide.with(programImage.context)
                .load(it.images.person)
                .into(programImage)
    }
}