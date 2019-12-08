package com.alorma.rac.data.db

import android.util.Log
import androidx.room.withTransaction
import com.alorma.rac.domain.model.Program
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgramsDbDataSource(
    private val dao: ProgramsDao,
    private val sectionsDao: SectionsDao,
    private val radioDatabase: RadioDatabase,
    private val mapper: ProgramsDbMapper
) {
    fun getPrograms(): Flow<List<Program>> {
        return dao.allPrograms().map {
            mapper.map(it)
        }
    }

    fun getProgram(programId: String): Flow<Program> {
        return dao.getProgramWithSection(programId).map {
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
        radioDatabase.withTransaction {
            val entities = mapper.mapToEntity(programs)
            dao.savePrograms(entities)

            val sections = programs.flatMap { program ->
                program.sections.map { section ->
                    mapper.mapToSectionEntity(section, program)
                }
            }

            sections.groupBy { it.programId }.forEach { t: String, u: List<SectionEntity> ->
                    Log.i("Alorma", "ProgramID: $t")
            }

            sectionsDao.saveSections(sections)
        }
    }

    suspend fun saveNow(it: Program) {
        dao.updateNow(mapper.mapToEntity(it))
    }
}