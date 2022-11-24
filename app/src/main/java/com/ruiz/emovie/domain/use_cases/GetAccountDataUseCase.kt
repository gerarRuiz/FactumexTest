package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.ApiResponseAccount
import com.ruiz.emovie.util.network.Result

class GetAccountDataUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(sessionId: String): Result<ApiResponseAccount> {

        try {
            val response = repository.getAccountData(sessionId = sessionId)
            if (!response.isSuccessful || response.body() == null) return Result.Error()
            return Result.Success(response.body()!!)
        } catch (e: Exception) {
            return Result.Error(error = e.message ?: "")
        }

    }

}