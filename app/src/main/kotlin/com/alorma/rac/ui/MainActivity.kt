package com.alorma.rac.ui

import android.os.Bundle
import androidx.lifecycle.observe
import com.alorma.rac.R
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
            when (it) {
                ListeningStatus.Nothing -> {
                    fab.setIconResource(R.drawable.ic_play)
                    fab.setText(R.string.listen_status_stop)
                }
                is ListeningStatus.Playing -> {
                    fab.setIconResource(R.drawable.ic_stop)
                    fab.setText(R.string.listen_status_play)
                }
            }
        }

        fab.setOnClickListener {
            listening.onStreamActionClick()
        }
    }
}
