package com.alorma.rac1.ui.programs

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.ui.common.BaseView
import com.alorma.rac1.ui.common.ProgramsAdapter
import com.alorma.rac1.ui.common.dsl
import kotlinx.android.synthetic.main.programs_fragment.*
import javax.inject.Inject

class ProgramsFragment : Fragment(), BaseView<ProgramsAction, ProgramsRoute, ProgramsState> {

    @Inject
    lateinit var presenter: ProgramsPresenter

    var listCallback: ListCallback? = null

    private val adapter: ProgramsAdapter by lazy {
        ProgramsAdapter {
            presenter reduce ProgramsAction.ProgramSelected(it)
        }
    }

    lateinit var manager: LinearLayoutManager

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

        toolbar.dsl {
            title = R.string.app_name
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

        if (adapter.itemCount == 0) {
            loadSchedule()
        }
    }

    private fun loadSchedule() {
        presenter reduce ProgramsAction.LoadSchedule()
    }

    private fun loadPrograms() {
        presenter reduce ProgramsAction.LoadPrograms()
    }

    override fun render(s: ProgramsState) {
        when (s) {
            is ProgramsState.ApplyDiff -> {
                recyclerView?.let {
                    val first = manager.findFirstVisibleItemPosition()
                    recyclerView.recycledViewPool.clear()
                    adapter.setItems(s.items)
                    s.diffResult.dispatchUpdatesTo(adapter)
                    recyclerView.scrollToPosition(first)
                }
            }
        }
    }

    override fun navigate(r: ProgramsRoute) {
        when (r) {
            is ProgramsRoute.OpenProgramDetail -> {
                listCallback?.onProgramSelected(r.it)
            }
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    interface ListCallback {
        fun onProgramSelected(programItem: ProgramItem)
    }
}