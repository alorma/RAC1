package com.alorma.rac1.ui.program

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramsRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.program_fragment.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LiveProgramFragment : Fragment() {

    private lateinit var infoFragment: ProgramInfoFragment
    private lateinit var podcastFragment: ProgramPodcastFragment

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var livePublisher: BehaviorSubject<ProgramItem>

    @Inject
    lateinit var programsRepository: ProgramsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this

        infoFragment = ProgramInfoFragment()
        podcastFragment = ProgramPodcastFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.program_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        selectInfoTab()
    }

    override fun onStart() {
        super.onStart()
        connectToLiveUpdate()
    }

    private fun connectToLiveUpdate() {
        disposable += livePublisher
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    infoFragment.updateProgram(it)
                    podcastFragment.updateProgram(it)
                }, {}, {})
    }

    private fun selectInfoTab() {
        childFragmentManager.beginTransaction().replace(R.id.content, infoFragment).commit()
    }

    private fun selectPodcastTab() {
        childFragmentManager.beginTransaction().replace(R.id.content, podcastFragment).commit()
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

}