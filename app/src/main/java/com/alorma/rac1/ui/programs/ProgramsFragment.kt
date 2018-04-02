package com.alorma.rac1.ui.programs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramsRepository
import com.alorma.rac1.data.net.ProgramDto
import com.alorma.rac1.ui.common.ProgramsAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.schedule_fragment.*
import javax.inject.Inject

class ProgramsFragment : Fragment() {

    @Inject
    lateinit var programsRepository: ProgramsRepository

    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }
    private val adapter : ProgramsAdapter by lazy { ProgramsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this
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
        disposable += programsRepository.getSchedule()
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    onItemsLoaded(it)
                }, {

                })
    }

    fun loadPrograms() {
        disposable += programsRepository.getPrograms()
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    onItemsLoaded(it)
                }, {

                })
    }

    private fun onItemsLoaded(items: List<ProgramDto>) {
        adapter.addAll(items)
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

}