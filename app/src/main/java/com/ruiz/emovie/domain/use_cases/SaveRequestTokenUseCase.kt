package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository

class SaveRequestTokenUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(requestToken: String){
        repository.saveRequestToken(requestToken)
    }

}