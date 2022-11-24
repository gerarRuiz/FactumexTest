package com.ruiz.emovie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ruiz.emovie.util.constants.Constants.MOVIE_TOP_RATED_DB_TABLE
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(tableName = MOVIE_TOP_RATED_DB_TABLE)
data class MovieTopRated(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val title: String,
    val original_language: String,
    val overview: String,
    val backdrop_path: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val genre_ids: List<Int>,
    @Transient
    val timeGeted: Long = System.currentTimeMillis(),
    @Transient
    var favorite: Boolean = false
)
