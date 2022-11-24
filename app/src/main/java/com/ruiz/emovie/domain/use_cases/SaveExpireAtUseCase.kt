package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository

class SaveExpireAtUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(expireAt: String){
        repository.saveExpireAt(expireAt)
    }

}