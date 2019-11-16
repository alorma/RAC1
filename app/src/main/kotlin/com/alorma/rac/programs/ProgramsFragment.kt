package com.alorma.rac.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.alorma.rac.R
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_programs.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProgramsFragment : BottomSheetDialogFragment() {

    private val programsViewModel: ProgramsViewModel by viewModel()
    private val adapter: ProgramsAdapter = ProgramsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_programs, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        programsViewModel.programs.observe(viewLifecycleOwner) {
            adapter.items = it
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireDialog() as? BottomSheetDialog)?.apply {
            dismissWithAnimation = true
            behavior.state = STATE_HALF_EXPANDED
        }
    }
}