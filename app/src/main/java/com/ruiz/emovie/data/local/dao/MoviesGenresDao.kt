package com.ruiz.emovie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruiz.emovie.domain.model.MoviesGenres

@Dao
interface MoviesGenresDao {

    @Query("SELECT * FROM movie_genres_db_table ORDER BY ID ASC")
    fun getAllMoviesGenres(): List<MoviesGenres>

    @Query("SELECT * FROM movie_genres_db_table where id IN(:genres)")
    fun getSelectedMovieGenres(genres: List<Int>): List<MoviesGenres>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoviesGenres(genres: List<MoviesGenres>)

    @Query("DELETE FROM movie_genres_db_table")
    suspend fun deleteAllMoviesGenres()

}