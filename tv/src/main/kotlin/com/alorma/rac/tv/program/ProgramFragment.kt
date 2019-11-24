package com.alorma.rac.tv.program

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import androidx.lifecycle.observe
import com.alorma.rac.data.api.ProgramApiModel
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

        programViewModel.programApiModel.observe(viewLifecycleOwner) { program ->
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

    private fun setupDetailsOverviewRow(programApiModel: ProgramApiModel) {
        setupBackground(programApiModel)
        setupDetail(programApiModel)
        setupSections(programApiModel)
    }

    private fun setupBackground(programApiModel: ProgramApiModel) {

    }

    private fun setupDetail(programApiModel: ProgramApiModel) {
        val row = DetailsOverviewRow(programApiModel).apply {
            actionsAdapter = ArrayObjectAdapter().apply {
                val listenAction = Action(0, "Escoltar")
                add(listenAction)
                setOnItemViewClickedListener { itemViewHolder,
                                               item,
                                               rowViewHolder,
                                               row ->


                }
            }
        }

        rowsAdapter.add(row)
    }

    private fun setupSections(programApiModel: ProgramApiModel) {
        programApiModel.sections.forEachIndexed { i, section ->
            val header = HeaderItem(i.toLong(), section.title)
            val listRowAdapter = ArrayObjectAdapter(ProgramSectionsPresenter(programApiModel, lifecycle))

            val sectionsRow = ListRow(header, listRowAdapter)
            rowsAdapter.add(sectionsRow)
        }
    }
}