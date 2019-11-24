package com.alorma.rac.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProgramEntity::class, SectionEntity::class],
    version = 1
)
abstract class RadioDatabase : RoomDatabase() {
    abstract fun programsDao(): ProgramsDao
}