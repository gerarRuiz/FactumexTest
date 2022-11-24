package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.MovieUpcoming

class GetSelectedUpComingMovieUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(movieId: Int): MovieUpcoming {
        return repository.getSelectedUpcomingMovie(movieId.toLong())
    }

}