package com.ruiz.emovie.domain.use_cases

import androidx.paging.PagingData
import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.MovieUpcoming
import kotlinx.coroutines.flow.Flow

class GetAllUpComingMoviesUseCase(
    private val repository: Repository
) {
    operator fun invoke(): Flow<PagingData<MovieUpcoming>> {
        return repository.getAllUpComingMovies()
    }
}