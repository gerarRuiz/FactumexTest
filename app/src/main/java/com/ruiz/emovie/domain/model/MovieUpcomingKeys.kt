package com.ruiz.emovie.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ruiz.emovie.util.constants.Constants.MOVIE_UPCOMING_KEYS_DB_TABLE

@Entity(tableName = MOVIE_UPCOMING_KEYS_DB_TABLE,)
data class MovieUpcomingKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val previousPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long?
)