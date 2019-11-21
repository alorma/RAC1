package com.alorma.rac.tv.base

import com.alorma.rac.tv.program.ProgramIntentFactory
import com.alorma.rac.tv.programs.ProgramsIntentFactory

class IntentFactory(
    val programs: ProgramsIntentFactory,
    val program: ProgramIntentFactory
)