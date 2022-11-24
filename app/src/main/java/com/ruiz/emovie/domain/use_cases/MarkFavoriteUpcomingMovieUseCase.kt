package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository

class MarkFavoriteUpcomingMovieUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(movieId: Int, favorite: Boolean) {
        repository.updateFavoriteUpcomingMovie(movieId.toLong(), favorite)
    }

}