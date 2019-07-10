package com.alorma.rac.programs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac.R
import com.alorma.rac.core.BaseFragment
import com.alorma.rac.extension.observeNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProgramsFragment : BaseFragment() {

    private val programsViewModel: ProgramsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_programs, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        programsViewModel.programs.observeNotNull(viewLifecycleOwner) {
            it.forEach {
                Log.i("Alorma", "Program: ${it.title}")
            }
        }
    }
}