package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.MoviesGenres

class GetAllGenresMoviesUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(ids: List<Int>): List<MoviesGenres> {
        return repository.getAllGenresMovies(ids)
    }

}