package com.ruiz.emovie.domain.use_cases

import androidx.paging.PagingData
import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.MovieTopRated
import kotlinx.coroutines.flow.Flow

class GetAllTopRatedMoviesUseCase(
    private val repository: Repository
) {

    operator fun invoke(): Flow<PagingData<MovieTopRated>> {
        return repository.getAllTopRatedMovies()
    }

}