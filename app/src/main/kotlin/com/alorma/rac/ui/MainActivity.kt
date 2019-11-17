package com.alorma.rac.ui

import android.os.Bundle
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.alorma.rac.R
import com.alorma.rac.extension.onClick
import com.alorma.rac.listening.ListeningStatus
import com.alorma.rac.listening.ListeningViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class MainActivity : BaseActivity() {

    private val listening: ListeningViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listening.lifecycle = this
        listening.status.observe(this) {
            val icon = when (it) {
                ListeningStatus.Nothing -> R.drawable.ic_play
                is ListeningStatus.Playing -> R.drawable.ic_stop
            }
            fab.setImageResource(icon)
        }

        fab.onClick {
            listening.onStreamActionClick()
        }

        liveTypeContainer.onClick {
            openPrograms()
        }
    }

    private fun openPrograms() {
        findNavController(R.id.nav_host_fragment)
            .navigate(R.id.action_nowFragment_to_programsFragment)
    }
}
