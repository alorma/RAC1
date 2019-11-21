package com.alorma.rac.tv.programs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.alorma.rac.tv.R

class ProgramsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programs)
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, ProgramsActivity::class.java)
    }
}
