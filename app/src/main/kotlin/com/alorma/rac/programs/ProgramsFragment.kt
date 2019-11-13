package com.alorma.rac.programs

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.doOnLayout
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.alorma.rac.R
import com.alorma.rac.core.BaseFragment
import com.alorma.rac.extension.doOnApplyWindowInsets
import com.alorma.rac.extension.getColorAttribute
import com.alorma.rac.extension.onClick
import com.alorma.rac.utils.lerp
import com.alorma.rac.utils.lerpArgb
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.android.synthetic.main.fragment_programs.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProgramsFragment : BaseFragment(R.layout.fragment_programs) {

    private val programsViewModel: ProgramsViewModel by viewModel()

    private val adapter: ProgramsAdapter = ProgramsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Programes"
        configureBottomSheet()

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        programsViewModel.programs.observe(viewLifecycleOwner) {
            adapter.items = it
        }
    }

    private fun configureBottomSheet() {
        val behavior = BottomSheetBehavior.from(programsSheet)
        val backCallback =
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, false) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        val sheetStartColor = programsSheet.context.getColorAttribute(R.attr.colorPrimarySurface)
        val sheetEndColor = programsSheet.context.getColorAttribute(R.attr.colorSurface)
        val sheetBackground = MaterialShapeDrawable(
            ShapeAppearanceModel.builder(
                programsSheet.context,
                R.style.ShapeAppearance_MinimizedSheet,
                0
            ).build()
        ).apply {
            fillColor = ColorStateList.valueOf(sheetStartColor)
        }
        programsSheet.background = sheetBackground
        programsSheet.doOnLayout {
            val peek = behavior.peekHeight
            val maxTranslationX = (it.width - peek).toFloat()
            programsSheet.translationX = (programsSheet.width - peek).toFloat()

            // Alter views based on the sheet expansion
            behavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    backCallback.isEnabled = newState == BottomSheetBehavior.STATE_EXPANDED
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    programsSheet.translationX =
                        lerp(maxTranslationX, 0f, 0f, 0.15f, slideOffset)
                    sheetBackground.interpolation = lerp(1f, 0f, 0f, 0.15f, slideOffset)
                    sheetBackground.fillColor = ColorStateList.valueOf(
                        lerpArgb(
                            sheetStartColor,
                            sheetEndColor,
                            0f,
                            0.3f,
                            slideOffset
                        )
                    )
                    programsIcon.alpha = lerp(1f, 0f, 0f, 0.15f, slideOffset)

                    sheetExpand.alpha = lerp(1f, 0f, 0f, 0.15f, slideOffset)
                    sheetExpand.visibility = if (slideOffset < 0.5) View.VISIBLE else View.GONE

                    recycler.alpha = lerp(0f, 1f, 0.2f, 0.8f, slideOffset)
                    toolbar.alpha = lerp(0f, 1f, 0.2f, 0.8f, slideOffset)
                    recycler.visibility = if (slideOffset < 0.5) View.GONE else View.VISIBLE
                    toolbar.visibility = if (slideOffset < 0.5) View.GONE else View.VISIBLE
                }
            })
            programsSheet.doOnApplyWindowInsets { _, insets, _, _ ->
                behavior.peekHeight = peek + insets.systemWindowInsetBottom
            }
        }

        sheetExpand.onClick {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        programsIcon.onClick {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}