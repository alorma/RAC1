package com.alorma.rac.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramsDao {

    @Query("SELECT * FROM programs")
    fun allPrograms(): Flow<List<ProgramEntity>>

    @Query("SELECT * FROM programs")
    fun allProgramsSync(): List<ProgramEntity>

    @Query("SELECT * FROM programs WHERE id = :id")
    fun getProgramById(id: String): Flow<ProgramEntity>

    @Transaction
    @Query("SELECT * FROM programs WHERE id = :id")
    fun getProgramWithSection(id: String): Flow<ProgramWithSectionsEntity>

    @Query("SELECT * FROM programs WHERE now = 1")
    fun now(): Flow<ProgramEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgram(program: ProgramEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePrograms(programs: List<ProgramEntity>)

    @Update(entity = ProgramEntity::class)
    suspend fun updateNow(updateNow: UpdateNow)

    @Transaction
    suspend fun updateNow(it: ProgramEntity) {
        allProgramsSync().map { UpdateNow(it.id, false) }.forEach {
            updateNow(it)
        }
        saveProgram(it.copy(isNow = true))
    }
}