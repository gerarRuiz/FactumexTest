package com.ruiz.emovie.data.remote

import com.ruiz.emovie.domain.model.*
import com.ruiz.emovie.util.constants.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = Constants.API_LENGUAGE,
        @Query("page") page: Int = 1,
    ): ApiResponseTopRated

    @GET("movie/upcoming")
    suspend fun getUpComingMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = Constants.API_LENGUAGE,
        @Query("page") page: Int = 1,
    ): ApiResponseUpComing

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = Constants.API_LENGUAGE,
        @Query("page") page: Int = 1,
    ): ApiResponsePopular

    @GET("/3/account/{account_id}/favorite/movies")
    suspend fun getMyFavoriteMovies(
        @Path("account_id") account_id: String,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = Constants.API_LENGUAGE,
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String
    ): ApiResponseMyFavoriteMovies

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = Constants.API_LENGUAGE,
    ): ApiResponseGenres

    @GET("/3/movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") movie_id: Int = 0,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ): Response<ApiResponseVideos>

    @GET("/3/authentication/token/new")
    suspend fun getRequestToken(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Response<ApiResponseRequestToken>

    @POST("/3/authentication/session/new")
    suspend fun getSessionId(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Body requestApiSessionId: RequestApiSessionId
    ): Response<ApiResponseSessionId>

    @GET("/3/account")
    suspend fun getAccountData(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("session_id") sessionId: String,
    ): Response<ApiResponseAccount>

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("/3/account/{account_id}/favorite")
    suspend fun markFavorite(
        @Path("account_id") account_id: String,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("session_id") sessionId: String,
        @Body request: ApiMarkFavoriteRequest
    ): Response<ApiMarkFavoriteResponse>

}