package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.MoviesPopular

class MarkFavoriteTopRatedMovieUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(movieId: Int, favorite: Boolean) {
        repository.updateTopRatedMovie(movieId.toLong(), favorite)
    }

}