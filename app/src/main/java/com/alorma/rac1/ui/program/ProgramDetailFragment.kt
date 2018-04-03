package com.alorma.rac1.ui.program

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.data.net.SessionsDto
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.SessionsRepository
import com.alorma.rac1.ui.common.dsl
import kotlinx.android.synthetic.main.program_detail_fragment.*
import javax.inject.Inject

class ProgramDetailFragment : Fragment() {

    lateinit var program: ProgramItem

    var detailCallback: DetailCallback? = null

    @Inject
    lateinit var sessionsRepository: SessionsRepository

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

        sessionsRepository.getSessions(program.id, program.sections[0].id)
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    onLoad(it)
                }, {

                })

    }

    private fun onLoad(it: SessionsDto) {

    }

    interface DetailCallback {
        fun onProgramDetailBack()
        fun onProgramDetailError(title: String)
    }
}