package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.ProgramsRepository
import com.alorma.rac.domain.model.Program

class ProgramsViewModel(
    private val repository: ProgramsRepository
) : BaseViewModel() {

    val programs: LiveData<List<Program>>
        get() = repository.getPrograms().asLiveData()

    val now: LiveData<Program?>
        get() = repository.getNow().asLiveData()
}