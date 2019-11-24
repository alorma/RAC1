package com.alorma.rac.data.db

import androidx.room.*

@Entity(
    indices = [Index("id")],
    tableName = "programs"
)
data class ProgramEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subtitle") val subtitle: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "schedule") val schedule: String?,
    @Embedded(prefix = "socialNetworks") val socialNetworks: SocialNetworksEntity?,
    @Embedded(prefix = "images") val images: ImagesEntity?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "now") val isNow: Boolean = false
)