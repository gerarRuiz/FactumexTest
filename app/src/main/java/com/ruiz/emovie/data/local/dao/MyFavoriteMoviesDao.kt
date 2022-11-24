package com.ruiz.emovie.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruiz.emovie.domain.model.MovieTopRated
import com.ruiz.emovie.domain.model.MoviesPopular
import com.ruiz.emovie.domain.model.MyFavoriteMovies

@Dao
interface MyFavoriteMoviesDao {

    @Query("SELECT * FROM movie_favorites_db_table ORDER BY timeGeted ASC")
    fun getAllMyFavoriteMovies(): PagingSource<Int, MyFavoriteMovies>

    @Query("SELECT * FROM movie_favorites_db_table WHERE ID=:movieId")
    fun getSelectedMyFavorite(movieId: Long): MyFavoriteMovies

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMyFavoriteMovies(movies: List<MyFavoriteMovies>)

    @Query("DELETE FROM movie_favorites_db_table")
    suspend fun deleteAllFavoriteMovies()

}