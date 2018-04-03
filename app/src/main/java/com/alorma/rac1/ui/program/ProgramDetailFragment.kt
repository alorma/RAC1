package com.alorma.rac1.ui.program

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramsRepository
import com.alorma.rac1.ui.common.dsl
import kotlinx.android.synthetic.main.program_detail_fragment.*
import javax.inject.Inject

class ProgramDetailFragment : Fragment() {

    lateinit var program: ProgramItem

    var detailCallback: DetailCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component inject this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.program_detail_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.dsl {
            back { action = { detailCallback?.onProgramDetailBack() } }
            title = program.title
        }
    }

    interface DetailCallback {
        fun onProgramDetailBack()
        fun onProgramDetailError(title: String)
    }
}