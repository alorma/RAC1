package com.alorma.rac.tv.programs

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.observe
import com.alorma.rac.data.api.Program
import com.alorma.rac.now.NowViewModel
import com.alorma.rac.programs.ProgramsViewModel
import com.alorma.rac.tv.R
import com.alorma.rac.tv.base.IntentFactory
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProgramsFragment : BrowseSupportFragment() {

    private val intentFactory: IntentFactory by inject()

    private val nowViewModel: NowViewModel by viewModel()
    private val programsViewModel: ProgramsViewModel by viewModel()

    private val nowRowAdapter by lazy {
        ArrayObjectAdapter(ProgramsAdapter(lifecycle))
    }
    private val programsRowAdapter by lazy {
        ArrayObjectAdapter(ProgramsAdapter(lifecycle))
    }

    private val rowsAdapter: ArrayObjectAdapter = ArrayObjectAdapter(ListRowPresenter())

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        title = "Rac1"

        configureColors()
        setupUi()

        connectData()
    }

    private fun configureColors() {
        val color = ContextCompat.getColor(
            requireContext(),
            R.color.fastlane_background
        )
        brandColor = color
        searchAffordanceColor = color
    }

    private fun setupUi() {
        val nowHeader = HeaderItem("En directe")
        rowsAdapter.add(0, ListRow(nowHeader, nowRowAdapter))

        val programsHeader = HeaderItem("Programes")
        rowsAdapter.add(1, ListRow(programsHeader, programsRowAdapter))

        adapter = rowsAdapter

        onItemViewClickedListener =
            OnItemViewClickedListener { _, item, _, _ ->
                (item as? Program)?.let { program ->
                    onProgramClick(program)
                }
            }
    }

    private fun onProgramClick(program: Program) {
        intentFactory.program.buildIntent(requireContext(), program)
    }

    private fun connectData() {
        nowViewModel.now.observe(viewLifecycleOwner) { program ->
            if (nowRowAdapter.unmodifiableList<Any>().isEmpty()) {
                nowRowAdapter.add(program)
            } else {
                nowRowAdapter.replace(0, program)
            }
        }

        programsViewModel.programs.observe(viewLifecycleOwner) { programsList ->
            programsList.forEach {
                programsRowAdapter.add(it)
            }
        }
    }
}