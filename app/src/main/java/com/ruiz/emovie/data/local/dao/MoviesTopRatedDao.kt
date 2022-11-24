package com.ruiz.emovie.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruiz.emovie.domain.model.MovieTopRated

@Dao
interface MoviesTopRatedDao {

    @Query("SELECT * FROM movie_top_rated_db_table ORDER BY timeGeted ASC")
    fun getAllMoviesTopRated(): PagingSource<Int, MovieTopRated>

    @Query("SELECT * FROM movie_top_rated_db_table WHERE ID=:movieId")
    fun getSelectedMovieTopRated(movieId: Long): MovieTopRated

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoviesTopRated(movies: List<MovieTopRated>)

    @Query("DELETE FROM movie_top_rated_db_table")
    suspend fun deleteAllMoviesTopRated()

    @Query("UPDATE movie_top_rated_db_table SET favorite=:favorite where id=:id")
    fun markFavorite(id: Long, favorite: Boolean)

}