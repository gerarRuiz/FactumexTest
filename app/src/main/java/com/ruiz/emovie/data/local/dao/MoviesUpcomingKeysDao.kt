package com.ruiz.emovie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruiz.emovie.domain.model.MovieTopRatedKeys
import com.ruiz.emovie.domain.model.MovieUpcomingKeys

@Dao
interface MoviesUpcomingKeysDao {

    @Query("SELECT * FROM movie_upcoming_keys_db_table where ID=:id")
    suspend fun getMovieUpcomingRemoteKeys(id: Long): MovieUpcomingKeys?

    @Query("SELECT * FROM movie_upcoming_keys_db_table")
    suspend fun getAllMovieUpcomingRemoteKeys(): List<MovieUpcomingKeys?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMoviesUpcomingRemoteKeys(movieUpcomingKeys: List<MovieUpcomingKeys>)

    @Query("DELETE FROM movie_upcoming_keys_db_table")
    suspend fun deleteAllMoviesUpcomingRemoteKeys()

}