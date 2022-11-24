package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import kotlinx.coroutines.flow.Flow

class ReadExpireAtUseCase(
    private val repository: Repository
) {

    operator fun invoke(): Flow<String> {
        return repository.readExpireAt()
    }

}