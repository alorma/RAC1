package com.alorma.rac.tv.program

import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import androidx.lifecycle.observe
import coil.Coil
import coil.api.load
import coil.size.PixelSize
import com.alorma.rac.data.api.Program
import com.alorma.rac.extension.dp
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

    private val mPresenterSelector = ClassPresenterSelector()
    private val mAdapter = ArrayObjectAdapter(mPresenterSelector)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        programViewModel.program.observe(viewLifecycleOwner) { program ->
            setupDetailsOverviewRow(program)
            setupDetailsOverviewRowPresenter()
            setupBackground(program)
            adapter = mAdapter
        }
    }

    private fun setupDetailsOverviewRow(program: Program) {
        val row = DetailsOverviewRow(program)
        //loadPersonImage(program, row)

        val actionAdapter = ArrayObjectAdapter()
        actionAdapter.add(
            Action(
                ACTION_SECTIONS,
                resources.getString(R.string.program_action_sections)
            )
        )
        row.actionsAdapter = actionAdapter
        mAdapter.add(row)
    }

    private fun loadPersonImage(
        program: Program,
        row: DetailsOverviewRow
    ) {
        Coil.load(requireContext(), program.images.program) {
            placeholder(R.drawable.default_background)
            size(PixelSize(DETAIL_THUMB_WIDTH.dp, DETAIL_THUMB_HEIGHT.dp))
            target {
                row.imageDrawable = it
            }
        }
    }

    private fun setupDetailsOverviewRowPresenter() {
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(ProgramDescriptionPresenter())
        mPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
    }

    private fun setupBackground(program: Program) {
        detailsBackground.enableParallax()
        Coil.load(requireContext(), program.images.itunes) {
            placeholder(R.drawable.default_background)
            lifecycle(viewLifecycleOwner)

            target {
                detailsBackground.coverBitmap = it.toBitmap()
            }
        }
    }

    companion object {
        private const val ACTION_SECTIONS = 1L
        private const val DETAIL_THUMB_WIDTH = 274
        private const val DETAIL_THUMB_HEIGHT = 274
    }
}