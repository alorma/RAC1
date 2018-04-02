package com.alorma.rac1.ui.programs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.ui.common.BaseView
import com.alorma.rac1.ui.common.ProgramsAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.schedule_fragment.*
import javax.inject.Inject

class ProgramsFragment : Fragment(), BaseView<ProgramsAction, ProgramsRoute, ProgramsState> {
    @Inject
    lateinit var presenter: ProgramsPresenter

    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }
    private val adapter: ProgramsAdapter by lazy { ProgramsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this
        presenter init this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.schedule_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
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
                adapter.setItems(s.items)
                s.diffResult.dispatchUpdatesTo(adapter)
            }
        }
    }

    override fun navigate(r: ProgramsRoute) {

    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

}