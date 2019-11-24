package com.alorma.rac.tv.programs

import android.content.Context
import android.content.Intent

class ProgramsIntentFactory {
    fun buildIntent(context: Context): Intent {
        return ProgramsActivity.createIntent(context)
    }
}