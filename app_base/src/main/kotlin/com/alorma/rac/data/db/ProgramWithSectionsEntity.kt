package com.alorma.rac.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class ProgramWithSectionsEntity(
    @Embedded val programEntity: ProgramEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "programId"
    )
    val sections: List<SectionEntity>
)