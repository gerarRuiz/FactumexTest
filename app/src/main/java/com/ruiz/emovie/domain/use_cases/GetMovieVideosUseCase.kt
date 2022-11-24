package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.ApiResponseVideos
import com.ruiz.emovie.util.network.Result

class GetMovieVideosUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(movieId: Int): Result<ApiResponseVideos> {
        try {
            val response = repository.getMovieVideos(movieId)
            if (!response.isSuccessful || response.body() == null) return Result.Error()
            return Result.Success(response.body()!!)
        } catch (e: Exception) {
            return Result.Error(error = e.message ?: "")
        }
    }

}