package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.ApiResponseSessionId
import com.ruiz.emovie.domain.model.RequestApiSessionId
import com.ruiz.emovie.util.network.Result

class GetSessionIdUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(requestApiSessionId: RequestApiSessionId): Result<ApiResponseSessionId> {

        try {
            val response = repository.getSessionId(requestApiSessionId = requestApiSessionId)
            if (!response.isSuccessful || response.body() == null) return Result.Error()
            return Result.Success(response.body()!!)
        } catch (e: Exception) {
            return Result.Error(error = e.message ?: "")
        }

    }

}