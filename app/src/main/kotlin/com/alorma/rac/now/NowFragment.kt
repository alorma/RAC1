package com.alorma.rac.now

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.alorma.rac.R
import com.alorma.rac.core.BaseFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_now.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NowFragment : BaseFragment() {

    private val nowViewModel: NowViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_now, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nowViewModel.lifecycle = viewLifecycleOwner

        nowViewModel.now.observe(viewLifecycleOwner) {
            programTitle.text = it.title.trim()
            programSubtitle.text = it.subtitle.trim()
            programHours.text = it.schedule.trim()
            it.images.person?.let {
                Glide.with(programPerson).load(it).into(programPerson)
            }
        }
    }
}