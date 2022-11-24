package com.ruiz.emovie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ruiz.emovie.util.constants.Constants.MOVIE_POPULAR_KEYS_DB_TABLE

@Entity(tableName = MOVIE_POPULAR_KEYS_DB_TABLE)
data class MoviesPopularKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val previousPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long?
)
