package com.alorma.rac1.ui.program

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramsRepository
import com.alorma.rac1.ui.common.dsl
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.program_fragment.*
import javax.inject.Inject

class ProgramActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ID = "extra_id"
        fun getIntent(context: Context, programId: String): Intent =
                Intent(context, ProgramActivity::class.java).apply {
                    putExtra(EXTRA_ID, programId)
                }
    }

    @Inject
    lateinit var programsRepository: ProgramsRepository

    private lateinit var infoFragment: ProgramInfoFragment
    private lateinit var podcastFragment: ProgramPodcastFragment

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.program_fragment)

        component inject this

        intent?.getStringExtra(EXTRA_ID)?.let { programId ->
            infoFragment = ProgramInfoFragment()
            podcastFragment = ProgramPodcastFragment()

            toolbar.dsl {
                back { action = { finish() } }
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> selectInfoTab()
                        1 -> selectPodcastTab()
                    }
                }
            })
            loadProgram(programId)
        } ?: finish()

    }

    private fun loadProgram(programId: String) {
        disposable += programsRepository.getProgram(programId)
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    onProgramSet(it)
                }, {})
    }

    private fun onProgramSet(it: ProgramItem?) {
        infoFragment.programItem = it
        podcastFragment.programItem = it
        selectInfoTab()
    }

    private fun selectInfoTab() {
        supportFragmentManager.beginTransaction().replace(R.id.content, infoFragment).commit()
    }

    private fun selectPodcastTab() {
        supportFragmentManager.beginTransaction().replace(R.id.content, podcastFragment).commit()
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

}