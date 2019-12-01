package com.alorma.rac.data.db

import com.alorma.rac.domain.model.Program
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgramsDbDataSource(
    private val dao: ProgramsDao,
    private val mapper: ProgramsDbMapper
) {
    fun getPrograms(): Flow<List<Program>> {
        return dao.allPrograms().map {
            mapper.map(it)
        }
    }

    fun getProgram(programId: String): Flow<Program> {
        return dao.getProgramById(programId).map {
            mapper.map(it)
        }
    }

    fun getNow(): Flow<Program?> {
        return dao.now().map {
            it?.let {
                mapper.map(it)
            }
        }
    }

    suspend fun savePrograms(programs: List<Program>) {
        val entities = mapper.mapToEntity(programs)
        dao.savePrograms(entities)
    }

    suspend fun saveNow(it: Program) {
        dao.updateNow(mapper.mapToEntity(it))
    }
}