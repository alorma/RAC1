package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.ProgramsRepository
import com.alorma.rac.domain.model.Program
import com.alorma.rac.domain.model.Section
import kotlinx.coroutines.flow.collect

class ProgramViewModel(
    programId: String,
    private val repository: ProgramsRepository
) : BaseViewModel() {

    val program: LiveData<Program> = liveData {
        repository.getProgram(programId).collect {
            emit(it)
            it.sections.forEach {
                loadSection(it)
            }
        }
    }

    private fun loadSection(it: Section) {

    }
}