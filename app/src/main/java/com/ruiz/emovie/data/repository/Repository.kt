package com.ruiz.emovie.data.repository

import androidx.paging.PagingData
import com.ruiz.emovie.domain.model.*
import com.ruiz.emovie.domain.repository.DataStoreOperations
import com.ruiz.emovie.domain.repository.LocalDataSource
import com.ruiz.emovie.domain.repository.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dataStoreOperations: DataStoreOperations
) {

    fun getAllTopRatedMovies(): Flow<PagingData<MovieTopRated>> {
        return remote.getAllTopRatedMovies()
    }

    fun getAllUpComingMovies(): Flow<PagingData<MovieUpcoming>> {
        return remote.getAllUpComingMovies()
    }

    fun getAllPopularMovies(): Flow<PagingData<MoviesPopular>> {
        return remote.getAllPopularMovies()
    }

    fun getAllMyFavoritesMovies(accountId: String, sessionId: String): Flow<PagingData<MyFavoriteMovies>> {
        return remote.getAllMyFavoriteMovies(accountId, sessionId)
    }

    suspend fun getAllGenresMovies(ids: List<Int>): List<MoviesGenres> {
        return remote.getAllGenresMovies(ids)
    }

    suspend fun getMovieVideos(idMovie: Int): Response<ApiResponseVideos> {
        return remote.getMovieVideos(movieId = idMovie)
    }

    suspend fun getRequestToken(): Response<ApiResponseRequestToken> {
        return remote.getRequestToken()
    }

    suspend fun getSessionId(requestApiSessionId: RequestApiSessionId): Response<ApiResponseSessionId> {
        return remote.getSessionId(requestApiSessionId = requestApiSessionId)
    }

    suspend fun getAccountData(sessionId: String): Response<ApiResponseAccount> {
        return remote.getAccountData(sessionId = sessionId)
    }

    suspend fun markFavoriteMovie(
        accountId: String,
        sessionId: String,
        apiMarkFavoriteRequest: ApiMarkFavoriteRequest
    ): Response<ApiMarkFavoriteResponse> {
        return remote.markFavoriteMovie(accountId, sessionId, apiMarkFavoriteRequest)
    }

    /**
     * Local
     */

    suspend fun getSelectedTopRatedMoview(idMovie: Long): MovieTopRated {
        return local.getSelectedTopRatedMovie(idMovie)
    }

    suspend fun updateTopRatedMovie(idMovie: Long, favorite: Boolean){
        local.updateTopRatedMovieFavorite(idMovie, favorite)
    }

    suspend fun getSelectedUpcomingMovie(idMovie: Long): MovieUpcoming {
        return local.getSelectedUpcomingMovie(idMovie)
    }

    suspend fun updateFavoriteUpcomingMovie(idMovie: Long, favorite: Boolean){
        local.updateUpcomingMovieFavorite(idMovie, favorite)
    }

    suspend fun getSelectedPopularMovie(idMovie: Long): MoviesPopular {
        return local.getSelectedPopularMovie(idMovie)
    }

    suspend fun updateFavoritePopularMovie(idMovie: Long, favorite: Boolean){
        local.updatePopularMovieFavorite(idMovie, favorite)
    }

    /**
     * Data Store
     */

    suspend fun saveRequestToken(requestToken: String) {
        dataStoreOperations.saveRequestToken(requestToken)
    }

    fun readRequesToken(): Flow<String> {
        return dataStoreOperations.readRequestToken()
    }

    suspend fun saveSessionId(sessionId: String) {
        dataStoreOperations.saveSessionId(sessionId)
    }

    fun readSessionId(): Flow<String> {
        return dataStoreOperations.readSessionId()
    }

    suspend fun saveExpireAt(expireAt: String) {
        dataStoreOperations.saveRequestTokenExpireAt(expireAt)
    }

    fun readExpireAt(): Flow<String> {
        return dataStoreOperations.readRequestTokenExpireAt()
    }

    suspend fun saveAccountId(accountId: String) {
        dataStoreOperations.saveAccountId(accountId)
    }

    fun readAccountId(): Flow<String> {
        return dataStoreOperations.readAccountId()
    }

}