package com.ruiz.emovie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ruiz.emovie.util.constants.Constants.MOVIE_FAVORITES_KEYS_DB_TABLE

@Entity(tableName = MOVIE_FAVORITES_KEYS_DB_TABLE,)
data class MyFavoriteMoviesKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val previousPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long?
)