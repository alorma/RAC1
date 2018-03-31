package com.alorma.rac1.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.net.Rac1Api
import com.alorma.rac1.net.ResponseNowDto
import com.luseen.spacenavigation.SpaceItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var rac1Api: Rac1Api

    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component inject this

        bottomBar.addSpaceItem(SpaceItem("Test1", R.drawable.ic_launcher_foreground))
        bottomBar.addSpaceItem(SpaceItem("Test2", R.drawable.ic_launcher_foreground))

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

    private fun onDataReceived(it: ResponseNowDto?) {

    }

    private fun onError(it: Throwable?) {

    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }
}
