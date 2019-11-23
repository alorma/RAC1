package com.alorma.rac.tv.program

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import androidx.lifecycle.observe
import com.alorma.rac.data.api.Program
import com.alorma.rac.programs.ProgramViewModel
import com.alorma.rac.tv.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProgramFragment : DetailsSupportFragment() {

    private val programViewModel: ProgramViewModel by viewModel() {
        val programId = requireActivity()
            .intent
            ?.extras
            ?.getString(ProgramActivity.EXTRA_PROGRAM_ID)!!
        parametersOf(programId)
    }


    private val detailsBackground: DetailsSupportFragmentBackgroundController by lazy {
        DetailsSupportFragmentBackgroundController(this)
    }

    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        detailsBackground.solidColor =
            ContextCompat.getColor(requireContext(), R.color.fastlane_background)

        buildAdapter()

        programViewModel.program.observe(viewLifecycleOwner) { program ->
            setupDetailsOverviewRow(program)
        }
    }

    private fun buildAdapter() {
        val selector = ClassPresenterSelector().apply {
            addClassPresenter(
                DetailsOverviewRow::class.java,
                FullWidthDetailsOverviewRowPresenter(ProgramDescriptionPresenter())
            )
            addClassPresenter(ListRow::class.java, ListRowPresenter())
        }
        rowsAdapter = ArrayObjectAdapter(selector)
        adapter = rowsAdapter
    }

    private fun setupDetailsOverviewRow(program: Program) {
        val row = DetailsOverviewRow(program).apply {
            actionsAdapter = ArrayObjectAdapter().apply {
                val listenAction = Action(0, "Escoltar")
                add(listenAction)
                setOnItemViewClickedListener { itemViewHolder,
                                               item,
                                               rowViewHolder,
                                               row ->

                    if (item is Action) {
                        if (item.id == 0L) {
                            Toast.makeText(
                                requireContext(),
                                "Escoltar ${program.title}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }
        }
        rowsAdapter.add(row)

        val listRowAdapter = ArrayObjectAdapter(ProgramSectionsPresenter(program, lifecycle))
        program.sections.forEach { listRowAdapter.add(it) }
        val header = HeaderItem(0, resources.getString(R.string.program_action_sections))
        val sectionsRow = ListRow(header, listRowAdapter)
        rowsAdapter.add(sectionsRow)
    }
}