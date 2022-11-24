package com.ruiz.emovie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruiz.emovie.domain.model.MovieTopRatedKeys

@Dao
interface MoviesTopRatedKeysDao {

    @Query("SELECT * FROM movie_top_rated_keys_db_table where ID=:id")
    suspend fun getMovieTopRatedRemoteKeys(id: Long): MovieTopRatedKeys?

    @Query("SELECT * FROM movie_top_rated_keys_db_table")
    suspend fun getAllMovieTopRatedRemoteKeys(): List<MovieTopRatedKeys?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMoviesTopRatedRemoteKeys(movieTopRatedKeys: List<MovieTopRatedKeys>)

    @Query("DELETE FROM movie_top_rated_keys_db_table")
    suspend fun deleteAllMoviesTopRatedRemoteKeys()

}