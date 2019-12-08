package com.alorma.rac.data.db

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = ProgramEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("programId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("id"), Index("programId")],
    tableName = "sections"
)
data class SectionEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "programId") val programId: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "title") val title: String
)