package com.alorma.rac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alorma.rac.listening.ListeningViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val listening: ListeningViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listening.lifecycle = this
    }
}
