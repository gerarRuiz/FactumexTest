package com.ruiz.emovie.presentation.fragments.home

import androidx.lifecycle.ViewModel
import com.ruiz.emovie.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    useCases: UseCases
) : ViewModel() {

    val getAllTopRatedMovies = useCases.getAllTopRatedMoviesUseCase()
    val getAllUpComingMoviesUseCase = useCases.getAllUpComingMoviesUseCase()
    val getAllPopularMoviesUseCase = useCases.getAllPopularMoviesUseCase()

}