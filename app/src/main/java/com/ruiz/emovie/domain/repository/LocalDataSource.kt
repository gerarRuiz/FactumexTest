package com.ruiz.emovie.domain.repository

import com.ruiz.emovie.domain.model.MovieTopRated
import com.ruiz.emovie.domain.model.MovieUpcoming
import com.ruiz.emovie.domain.model.MoviesGenres
import com.ruiz.emovie.domain.model.MoviesPopular

interface LocalDataSource {

    suspend fun getSelectedTopRatedMovie(idMovie: Long): MovieTopRated

    suspend fun updateTopRatedMovieFavorite(idMovie: Long, favorite: Boolean)

    suspend fun getSelectedUpcomingMovie(idMovie: Long): MovieUpcoming

    suspend fun updateUpcomingMovieFavorite(idMovie: Long, favorite: Boolean)

    suspend fun getSelectedPopularMovie(idMovie: Long): MoviesPopular

    suspend fun updatePopularMovieFavorite(idMovie: Long, favorite: Boolean)

    suspend fun getSelectedGenres(idGenres: List<Int>): List<MoviesGenres>

}