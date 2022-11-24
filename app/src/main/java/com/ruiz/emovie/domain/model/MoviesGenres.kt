package com.ruiz.emovie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ruiz.emovie.util.constants.Constants.MOVIE_GENRES_DB_TABLE
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = MOVIE_GENRES_DB_TABLE)
data class MoviesGenres(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String
)
