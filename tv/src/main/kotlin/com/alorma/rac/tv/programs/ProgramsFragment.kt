package com.alorma.rac.tv.programs

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import com.alorma.rac.domain.model.Program
import com.alorma.rac.programs.ProgramsViewModel
import com.alorma.rac.tv.R
import com.alorma.rac.tv.base.IntentFactory
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProgramsFragment : BrowseSupportFragment() {

    private val intentFactory: IntentFactory by inject()

    private val programsViewModel: ProgramsViewModel by viewModel()

    private val nowRowAdapter by lazy {
        ArrayObjectAdapter(ProgramsPresenter(lifecycle))
    }
    private val programsRowAdapter by lazy {
        ArrayObjectAdapter(ProgramsPresenter(lifecycle))
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
        val intent = intentFactory.program.buildIntent(requireContext(), program)
        startActivity(intent)
    }

    private fun connectData() {
        programsViewModel.now.distinctUntilChanged().observe(viewLifecycleOwner) {
            it?.let { program ->
                nowRowAdapter.clear()
                if (nowRowAdapter.unmodifiableList<Any>().isEmpty()) {
                    nowRowAdapter.add(program)
                } else {
                    nowRowAdapter.replace(0, program)
                }
            }
        }

        programsViewModel.programs.distinctUntilChanged()
            .observe(viewLifecycleOwner) { programsList ->
                programsRowAdapter.clear()
                programsList.forEach {
                    programsRowAdapter.add(it)
                }
            }
    }
}