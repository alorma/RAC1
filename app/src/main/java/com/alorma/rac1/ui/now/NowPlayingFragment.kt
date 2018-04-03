package com.alorma.rac1.ui.now

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.service.Play
import com.alorma.rac1.service.Stop
import com.alorma.rac1.service.StreamPlayback
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.now_playing_fragment.*
import javax.inject.Inject

class NowPlayingFragment : Fragment() {

    @Inject
    lateinit var playbackPublisher: PublishSubject<StreamPlayback>

    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.now_playing_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable += playbackPublisher.subscribeOnIO().observeOnUI().subscribe({
            onStreamPlayback(it)
        }, {

        })
    }

    private fun onStreamPlayback(it: StreamPlayback) {
        when (it) {
            is Play -> showPlay(it)
            Stop -> clearProgram()
        }
    }

    private fun showPlay(it: Play) {
        title.text = it.programItem.title
        Glide.with(avatar.context)
                .load(it.programItem.images.itunes)
                .into(avatar)
    }

    private fun clearProgram() {
        title.text = ""
        avatar.setImageBitmap(null)
    }
}