package com.alorma.rac.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    foreignKeys = [ForeignKey(
        entity = ProgramEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("programId"),
        onDelete = ForeignKey.CASCADE
    )],
    primaryKeys = ["sectionId", "programId"],
    indices = [Index("sectionId"), Index("programId")],
    tableName = "sections"
)
data class SectionEntity(
    @ColumnInfo(name = "sectionId") val id: String,
    @ColumnInfo(name = "programId") val programId: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "title") val title: String
)