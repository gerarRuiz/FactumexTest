package com.ruiz.emovie.domain.use_cases

import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.MovieTopRated

class GetSelectedTopRatedMovieUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(movieId: Int): MovieTopRated{
        return repository.getSelectedTopRatedMoview(movieId.toLong())
    }

}