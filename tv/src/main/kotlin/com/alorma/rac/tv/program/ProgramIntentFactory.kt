package com.alorma.rac.tv.program

import android.content.Context
import android.content.Intent
import com.alorma.rac.domain.model.Program

class ProgramIntentFactory {
    fun buildIntent(context: Context, program: Program): Intent {
        return ProgramActivity.createIntent(context, program.id)
    }
}