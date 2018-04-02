package com.alorma.rac1.ui.programs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.ui.common.BaseView
import com.alorma.rac1.ui.common.ProgramsAdapter
import kotlinx.android.synthetic.main.programs_fragment.*
import javax.inject.Inject

class ProgramsFragment : Fragment(), BaseView<ProgramsAction, ProgramsRoute, ProgramsState> {

    @Inject
    lateinit var presenter: ProgramsPresenter

    private val adapter: ProgramsAdapter by lazy {
        ProgramsAdapter {
            presenter reduce ProgramsAction.ProgramSelected(it)
        }
    }

    private val manager: LinearLayoutManager by lazy { LinearLayoutManager(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this
        presenter init this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.programs_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
    }

    fun loadSchedule() {
        presenter reduce ProgramsAction.LoadSchedule()
    }

    fun loadPrograms() {
        presenter reduce ProgramsAction.LoadPrograms()
    }

    override fun render(s: ProgramsState) {
        when (s) {
            is ProgramsState.ApplyDiff -> {
                val first = manager.findFirstVisibleItemPosition()
                adapter.setItems(s.items)
                s.diffResult.dispatchUpdatesTo(adapter)
                recyclerView.scrollToPosition(first)
            }
        }
    }

    override fun navigate(r: ProgramsRoute) {
        when(r) {
            is ProgramsRoute.OpenProgramDetail -> {
                Toast.makeText(context, r.programDto.title, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}