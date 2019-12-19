package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.ProgramsRepository
import com.alorma.rac.domain.model.Program

class ProgramViewModel(
    private val programId: String,
    private val repository: ProgramsRepository
) : BaseViewModel() {

    val program: LiveData<Program>
        get() = repository.getProgram(programId).asLiveData()
}