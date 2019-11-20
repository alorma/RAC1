package com.alorma.rac.tv

import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.observe
import com.alorma.rac.data.api.Program
import com.alorma.rac.now.NowViewModel
import com.alorma.rac.programs.ProgramsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProgramsFragment : BrowseSupportFragment() {

    private val rowsAdapter: ArrayObjectAdapter = ArrayObjectAdapter(ListRowPresenter())

    private val nowViewModel: NowViewModel by viewModel()
    private val programsViewModel: ProgramsViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = rowsAdapter

        title = "Rac1"

        nowViewModel.now.observe(viewLifecycleOwner) { program ->
            buildNowProgramRow(program)
        }

        programsViewModel.programs.observe(viewLifecycleOwner) { programsList ->
            buildProgramsRows(programsList)
        }
    }

    private fun buildNowProgramRow(program: Program) {
        val programsAdapter = ProgramsAdapter(lifecycle)
        val listRowAdapter = ArrayObjectAdapter(programsAdapter)
        listRowAdapter.add(program)
        val header = HeaderItem("En directe")
        rowsAdapter.add(0, ListRow(header, listRowAdapter))
    }

    private fun buildProgramsRows(programsList: List<Program>) {
        val programsAdapter = ProgramsAdapter(lifecycle)
        val listRowAdapter = ArrayObjectAdapter(programsAdapter)
        programsList.forEach {
            listRowAdapter.add(it)
        }
        val header = HeaderItem("Programes")
        val position = if (rowsAdapter.unmodifiableList<Any>().isEmpty()) {
            0
        } else {
            1
        }
        rowsAdapter.add(position, ListRow(header, listRowAdapter))
    }

}