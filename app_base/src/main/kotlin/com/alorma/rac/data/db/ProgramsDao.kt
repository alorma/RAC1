package com.alorma.rac.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProgramsDao {

    @Query("SELECT * FROM programs")
    suspend fun allPrograms(): List<ProgramEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePrograms(programs: List<ProgramEntity>)
}