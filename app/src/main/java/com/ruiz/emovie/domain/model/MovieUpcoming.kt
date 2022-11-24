package com.ruiz.emovie.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ruiz.emovie.util.constants.Constants.MOVIE_UPCOMING_DB_TABLE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(tableName = MOVIE_UPCOMING_DB_TABLE,)
data class MovieUpcoming(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val title: String? = null,
    val original_language: String? = null,
    val overview: String? = null,
    val backdrop_path: String? = null,
    val poster_path: String? = null,
    val release_date: String? = null,
    val vote_average: Double? = null,
    val genre_ids: List<Int>? = null,
    @Transient
    val timeGeted: Long = System.currentTimeMillis(),
    @Transient
    var favorite: Boolean = false
)