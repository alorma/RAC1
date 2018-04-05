package com.alorma.rac1.ui.program

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import kotlinx.android.synthetic.main.program_fragment.*

class ProgramFragment : Fragment() {

    private lateinit var infoFragment: Fragment
    private lateinit var podcastFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        infoFragment = ProgramInfoFragment()
        podcastFragment = ProgramProdcastFragment()
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
                when(tab.position) {
                    0 -> selectInfoTab()
                    1 -> selectPodcastTab()
                }
            }
        })
        selectInfoTab()
        selectPodcastTab()
    }

    private fun selectInfoTab() {
        childFragmentManager.beginTransaction().replace(R.id.content, infoFragment).commit()
    }

    private fun selectPodcastTab() {

    }

}