package com.alorma.rac.tv.program

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import androidx.lifecycle.observe
import com.alorma.rac.domain.model.Program
import com.alorma.rac.programs.ProgramViewModel
import com.alorma.rac.tv.R
import com.alorma.rac.tv.base.createActionsAdapter
import com.alorma.rac.tv.base.title
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
            setupDetail(program)
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

    private fun setupDetail(program: Program) {
        val row = DetailsOverviewRow(program).apply {

            actionsAdapter = createActionsAdapter(
                0L to "Escoltar",
                1L to "Seccions"
            ) {
                Toast.makeText(requireContext(), it.title, Toast.LENGTH_SHORT).show()
            }
        }

        rowsAdapter.add(row)
    }
}