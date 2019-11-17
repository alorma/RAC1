package com.alorma.rac.now

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import coil.api.load
import com.alorma.rac.R
import com.alorma.rac.core.BaseFragment
import kotlinx.android.synthetic.main.fragment_now.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NowFragment : BaseFragment(R.layout.fragment_now) {

    private val nowViewModel: NowViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nowViewModel.lifecycle = viewLifecycleOwner

        nowViewModel.now.observe(viewLifecycleOwner) {
            programTitle.text = it.title.trim()
            programSubtitle.text = it.subtitle.trim()
            programHours.text = it.schedule.trim()
            it.images.person?.let { image ->
                programPerson.load(image)
            }
        }
    }
}