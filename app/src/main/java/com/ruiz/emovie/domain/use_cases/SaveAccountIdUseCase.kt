package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository

class SaveAccountIdUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(accountId: String){
        repository.saveAccountId(accountId)
    }

}