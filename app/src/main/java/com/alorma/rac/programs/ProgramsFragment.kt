package com.alorma.rac.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alorma.rac.R
import com.alorma.rac.core.BaseFragment
import com.alorma.rac.extension.observeNotNull
import kotlinx.android.synthetic.main.fragment_programs.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProgramsFragment : BaseFragment() {

    private val programsViewModel: ProgramsViewModel by viewModel()

    private val adapter: ProgramsAdapter = ProgramsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_programs, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        programsViewModel.programs.observeNotNull(viewLifecycleOwner) {
            adapter.items = it
        }
    }
}