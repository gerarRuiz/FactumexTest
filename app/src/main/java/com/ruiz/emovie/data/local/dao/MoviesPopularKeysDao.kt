package com.ruiz.emovie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruiz.emovie.domain.model.MovieTopRatedKeys
import com.ruiz.emovie.domain.model.MoviesPopularKeys

@Dao
interface MoviesPopularKeysDao {

    @Query("SELECT * FROM movie_popular_keys_db_table where ID=:id")
    suspend fun getMoviePopularRemoteKeys(id: Long): MoviesPopularKeys?

    @Query("SELECT * FROM movie_popular_keys_db_table")
    suspend fun getAllMoviePopularKeys(): List<MoviesPopularKeys?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMoviesPopularRemoteKeys(movieTopRatedKeys: List<MoviesPopularKeys>)

    @Query("DELETE FROM movie_popular_keys_db_table")
    suspend fun deleteAllMoviesPopularRemoteKeys()

}