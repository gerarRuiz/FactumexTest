package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository

class SaveSessionIdUseCase(
    private val repository: Repository
){

    suspend operator fun invoke(sessionId: String){
        repository.saveSessionId(sessionId)
    }

}