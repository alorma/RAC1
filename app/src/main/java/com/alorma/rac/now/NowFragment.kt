package com.alorma.rac.now

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alorma.rac.R
import com.alorma.rac.core.BaseFragment
import com.alorma.rac.extension.observeNotNull
import com.alorma.rac.listening.ListeningViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NowFragment : BaseFragment() {

    private val listening: ListeningViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_now, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listening.now.observeNotNull(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Listen status: $it", Toast.LENGTH_SHORT).show()
        }
    }
}