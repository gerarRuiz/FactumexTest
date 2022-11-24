package com.ruiz.emovie.domain.use_cases

import androidx.paging.PagingData
import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.MoviesPopular
import kotlinx.coroutines.flow.Flow

class GetAllPopularMoviesUseCase(
    private val repository: Repository
) {

    operator fun invoke(): Flow<PagingData<MoviesPopular>> {
        return repository.getAllPopularMovies()
    }

}