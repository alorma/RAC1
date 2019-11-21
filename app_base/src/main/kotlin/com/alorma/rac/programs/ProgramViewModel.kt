package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.api.Program

class ProgramViewModel() : BaseViewModel() {

    val program: LiveData<Program> = liveData {

    }
}