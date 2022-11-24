package com.ruiz.emovie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruiz.emovie.domain.model.MovieTopRatedKeys
import com.ruiz.emovie.domain.model.MoviesPopularKeys
import com.ruiz.emovie.domain.model.MyFavoriteMoviesKeys

@Dao
interface MyFavoriteMoviesKeysDao {

    @Query("SELECT * FROM movie_favorites_keys_db_table where ID=:id")
    suspend fun getMovieFavoriteRemoteKeys(id: Long): MyFavoriteMoviesKeys?

    @Query("SELECT * FROM movie_favorites_keys_db_table")
    suspend fun getAllMovieFavoriteKeys(): List<MyFavoriteMoviesKeys?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMoviesFavoriteRemoteKeys(movieFavoriteKeys: List<MyFavoriteMoviesKeys>)

    @Query("DELETE FROM movie_favorites_keys_db_table")
    suspend fun deleteAllMoviesFavoriteRemoteKeys()

}