package com.alorma.rac1.ui.program

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramSection
import com.alorma.rac1.ui.common.BaseView
import com.alorma.rac1.ui.common.dsl
import kotlinx.android.synthetic.main.program_detail_fragment.*
import javax.inject.Inject

class ProgramDetailFragment : Fragment(), BaseView<ProgramDetailAction, ProgramDetailRoute, ProgramDetailState> {

    @Inject
    lateinit var presenter: ProgramDetailPresenter

    lateinit var program: ProgramItem

    var detailCallback: DetailCallback? = null

    private val adapter: SectionsAdapter by lazy {
        SectionsAdapter {
            onSectionClick(it)
        }
    }
    lateinit var manager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this
        presenter init this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.program_detail_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.dsl {
            back { action = { detailCallback?.onProgramDetailBack() } }
            title = program.title
            subTitle = program.subtitle
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun supportsPredictiveItemAnimations(): Boolean {
                return false
            }
        }.also { manager = it }
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            context?.let {
                setDrawable(ColorDrawable(ContextCompat.getColor(it, R.color.very_dark_grey)))
            }
        })

        presenter reduce ProgramDetailAction.Load(program)
    }

    override fun render(s: ProgramDetailState) {
        when (s) {
            is ProgramDetailState.Success -> onSuccess(s.sections)
            is ProgramDetailState.SessionsLoaded -> onSessionsLoaded(s)
        }
    }

    private fun onSuccess(it: Map<ProgramSection, List<SessionDto>>) {
        adapter.setItems(it)
    }

    private fun onSessionsLoaded(s: ProgramDetailState.SessionsLoaded) {
        adapter.updateItem(s.section, s.sessions)
    }

    override fun navigate(r: ProgramDetailRoute) {

    }


    private fun onSectionClick(it: ProgramSection) {
        presenter reduce ProgramDetailAction.LoadSection(it)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    interface DetailCallback {
        fun onProgramDetailBack()
        fun onProgramDetailError(title: String)
    }
}