package com.alorma.rac.tv.program

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.alorma.rac.tv.R

class ProgramActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programs)
    }

    companion object {
        const val EXTRA_PROGRAM_ID = "extra_program_id"
        fun createIntent(context: Context, programId: String): Intent {
            return Intent(context, ProgramActivity::class.java)
                .putExtra(EXTRA_PROGRAM_ID, programId)
        }
    }
}