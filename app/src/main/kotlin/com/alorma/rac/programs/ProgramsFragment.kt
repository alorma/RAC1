package com.alorma.rac.programs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.alorma.rac.R
import com.alorma.rac.core.BaseFragment
import kotlinx.android.synthetic.main.fragment_programs.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProgramsFragment : BaseFragment(R.layout.fragment_programs) {

    private val programsViewModel: ProgramsViewModel by viewModel()

    private val adapter: ProgramsAdapter = ProgramsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        programsViewModel.programs.observe(viewLifecycleOwner) {
            adapter.items = it
            it.forEach {
                Log.i("Alorma", it.title)
            }
        }
    }
}