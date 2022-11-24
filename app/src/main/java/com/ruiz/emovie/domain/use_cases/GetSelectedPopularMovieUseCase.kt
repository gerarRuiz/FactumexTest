package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.MoviesPopular

class GetSelectedPopularMovieUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(movieId: Int): MoviesPopular {
        return repository.getSelectedPopularMovie(movieId.toLong())
    }

}