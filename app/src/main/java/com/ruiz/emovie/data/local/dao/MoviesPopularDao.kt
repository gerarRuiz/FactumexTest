package com.ruiz.emovie.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruiz.emovie.domain.model.MovieTopRated
import com.ruiz.emovie.domain.model.MoviesPopular

@Dao
interface MoviesPopularDao {

    @Query("SELECT * FROM movie_popular_db_table ORDER BY timeGeted ASC")
    fun getAllPopularMovies(): PagingSource<Int, MoviesPopular>

    @Query("SELECT * FROM movie_popular_db_table WHERE ID=:movieId")
    fun getSelectedPopularMovie(movieId: Long): MoviesPopular

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPopularMovies(movies: List<MoviesPopular>)

    @Query("DELETE FROM movie_popular_db_table")
    suspend fun deleteAllPopularMovies()

    @Query("UPDATE movie_popular_db_table SET favorite=:favorite where id=:id")
    fun markFavorite(id: Long, favorite: Boolean)

}