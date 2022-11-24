package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.ApiResponseRequestToken
import com.ruiz.emovie.util.network.Result

class GetRequestTokenUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(): Result<ApiResponseRequestToken> {

        try {
            val response = repository.getRequestToken()
            if (!response.isSuccessful || response.body() == null) return Result.Error()
            return Result.Success(response.body()!!)
        }catch (e: Exception){
            return Result.Error(error = e.message ?: "")
        }

    }

}