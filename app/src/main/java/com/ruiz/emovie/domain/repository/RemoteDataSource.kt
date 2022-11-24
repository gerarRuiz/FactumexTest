package com.ruiz.emovie.domain.repository

import androidx.paging.PagingData
import com.ruiz.emovie.domain.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {

    fun getAllTopRatedMovies(): Flow<PagingData<MovieTopRated>>

    fun getAllUpComingMovies(): Flow<PagingData<MovieUpcoming>>

    fun getAllPopularMovies(): Flow<PagingData<MoviesPopular>>

    fun getAllMyFavoriteMovies(accountId: String, sessionId: String): Flow<PagingData<MyFavoriteMovies>>

    suspend fun getAllGenresMovies(ids: List<Int>): List<MoviesGenres>

    suspend fun getMovieVideos(movieId: Int): Response<ApiResponseVideos>

    suspend fun getRequestToken(): Response<ApiResponseRequestToken>

    suspend fun getSessionId(requestApiSessionId: RequestApiSessionId): Response<ApiResponseSessionId>

    suspend fun getAccountData(sessionId: String): Response<ApiResponseAccount>

    suspend fun markFavoriteMovie(accountId: String, sessionId: String, apiMarkFavoriteRequest: ApiMarkFavoriteRequest): Response<ApiMarkFavoriteResponse>

}