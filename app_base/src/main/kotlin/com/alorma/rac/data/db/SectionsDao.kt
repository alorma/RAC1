package com.alorma.rac.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface SectionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSections(sections: List<SectionEntity>)

}