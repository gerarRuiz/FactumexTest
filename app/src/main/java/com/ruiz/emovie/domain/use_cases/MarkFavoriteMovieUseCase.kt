package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.ApiMarkFavoriteRequest
import com.ruiz.emovie.domain.model.ApiMarkFavoriteResponse
import com.ruiz.emovie.domain.model.ApiResponseVideos
import com.ruiz.emovie.util.network.Result

class MarkFavoriteMovieUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(accountId: String, sessionId: String, request: ApiMarkFavoriteRequest): Result<ApiMarkFavoriteResponse> {
        try {
            val response = repository.markFavoriteMovie(accountId = accountId, sessionId = sessionId, apiMarkFavoriteRequest = request)
            if (!response.isSuccessful || response.body() == null) return Result.Error()
            return Result.Success(response.body()!!)
        } catch (e: Exception) {
            return Result.Error(error = e.message ?: "")
        }
    }

}