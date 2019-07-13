package com.alorma.rac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.alorma.rac.extension.observeNotNull
import com.alorma.rac.listening.ListeningStatus
import com.alorma.rac.listening.ListeningViewModel
import com.alorma.rac.programs.ProgramsFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val listening: ListeningViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listening.lifecycle = this
        listening.status.observeNotNull(this) {
            when (it) {
                ListeningStatus.Nothing -> {
                    fab.setImageResource(R.drawable.ic_play)
                }
                is ListeningStatus.Playing -> {
                    fab.setImageResource(R.drawable.ic_stop)
                }
            }
        }

        fab.setOnClickListener {
            listening.onStreamActionClick()
        }

        bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_programs -> {
                    val openPrograms = ProgramsFragmentDirections.openPrograms()
                    findNavController(R.id.nav_host_fragment).navigate(openPrograms)
                    true
                }
                else ->
                    false
            }
        }
    }
}
