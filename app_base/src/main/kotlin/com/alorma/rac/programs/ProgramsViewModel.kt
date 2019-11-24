package com.alorma.rac.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.alorma.rac.core.BaseViewModel
import com.alorma.rac.data.db.ProgramsDao
import com.alorma.rac.data.db.ProgramsDbMapper
import com.alorma.rac.domain.model.Program

class ProgramsViewModel(
    private val programsDao: ProgramsDao,
    private val dbMapper: ProgramsDbMapper
) : BaseViewModel() {

    val programs: LiveData<List<Program>> = liveData {

        val allProgramsEntities = programsDao.allPrograms()
        val programs = dbMapper.map(allProgramsEntities)
        emit(programs)

    }
}