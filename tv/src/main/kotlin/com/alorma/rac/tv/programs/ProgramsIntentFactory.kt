package com.alorma.rac.tv.programs

import android.content.Context
import android.content.Intent
import com.alorma.rac.data.api.Program
import com.alorma.rac.tv.programs.ProgramsActivity

class ProgramsIntentFactory {
    fun buildIntent(context: Context): Intent {
        return ProgramsActivity.createIntent(context)
    }
}