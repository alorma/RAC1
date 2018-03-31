package com.alorma.rac1.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.net.Rac1Api
import com.alorma.rac1.net.ResponseNowDto
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var rac1Api: Rac1Api

    var isPlaying: Boolean = false

    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component inject this

        with(bottomBar) {
            configScheduleButton()
            configCenterButton()
            configProgramsButton()

            setSpaceOnClickListener(object : SpaceOnClickListener {
                override fun onCentreButtonClick() {
                    if (isPlaying) {
                        // TODO -> pause
                        setPlayIcon()
                    } else {
                        // TODO -> play
                        setPauseIcon()
                    }
                    isPlaying = isPlaying.not()
                }

                override fun onItemReselected(itemIndex: Int, itemName: String?) {

                }

                override fun onItemClick(itemIndex: Int, itemName: String?) {

                }

            })
        }

        disposable += rac1Api.now()
                .subscribeOnIO()
                .observeOnUI()
                .subscribe(
                        { onDataReceived(it) },
                        {
                            onError(it)
                        }
                )
    }

    private fun SpaceNavigationView.setPauseIcon() {
        changeCenterButtonIcon(R.drawable.ic_pause)
    }

    private fun SpaceNavigationView.setPlayIcon() {
        changeCenterButtonIcon(R.drawable.ic_play)
        (ContextCompat.getColor(this@MainActivity, R.color.white))
    }

    private fun SpaceNavigationView.configScheduleButton() {
        val space = getSpace(R.string.navigation_schedule, R.drawable.ic_schedule)
        addSpaceItem(space)
    }

    private fun SpaceNavigationView.configProgramsButton() {
        val space = getSpace(R.string.navigation_programs, R.drawable.ic_list)
        addSpaceItem(space)
    }

    private fun SpaceNavigationView.configCenterButton() {
        setCentreButtonIcon(R.drawable.ic_play)
        setCentreButtonColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
        setInActiveCentreButtonIconColor(ContextCompat.getColor(this@MainActivity, R.color.white))

    }

    private fun getSpace(title: Int, icon: Int): SpaceItem = SpaceItem(resources.getString(title), icon)

    private fun onDataReceived(it: ResponseNowDto?) {

    }

    private fun onError(it: Throwable?) {

    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }
}
