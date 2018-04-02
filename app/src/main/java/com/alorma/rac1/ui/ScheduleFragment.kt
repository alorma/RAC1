package com.alorma.rac1.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramsRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ScheduleFragment : Fragment() {

    @Inject
    lateinit var programsRepository: ProgramsRepository

    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.schedule_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable += programsRepository.getSchedule()
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    it.forEach {
                        Log.i("Alorma", "$it")
                    }
                }, {

                })
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

}