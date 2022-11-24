package com.ruiz.emovie.presentation.fragments.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruiz.emovie.domain.model.*
import com.ruiz.emovie.domain.use_cases.UseCases
import com.ruiz.emovie.util.extensions.asLiveData
import com.ruiz.emovie.util.extensions.update
import com.ruiz.emovie.util.network.Result
import com.ruiz.emovie.util.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _selectedTopRatedMovie: MutableStateFlow<MovieTopRated?> = MutableStateFlow(null)
    val selectedTopRatedMovie: MutableStateFlow<MovieTopRated?> = _selectedTopRatedMovie

    private val _selectedUpcomingMovie: MutableStateFlow<MovieUpcoming?> = MutableStateFlow(null)
    val selectedUpcomingMovie: MutableStateFlow<MovieUpcoming?> = _selectedUpcomingMovie

    private val _selectedPopularMovie: MutableStateFlow<MoviesPopular?> = MutableStateFlow(null)
    val selectedPopularMovie: MutableStateFlow<MoviesPopular?> = _selectedPopularMovie

    private val _genresMovies: MutableStateFlow<List<MoviesGenres>?> = MutableStateFlow(null)
    val genresMovies: MutableStateFlow<List<MoviesGenres>?> = _genresMovies

    private val _sessionId = MutableStateFlow("")
    val sessionId: StateFlow<String> = _sessionId

    private val _accountId = MutableStateFlow("")
    val accountId: StateFlow<String> = _accountId

    private val _markFavoriteState: MutableLiveData<UIState<ApiMarkFavoriteResponse>?> =
        MutableLiveData(null)
    val markFavoriteState = _markFavoriteState.asLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _sessionId.value = useCases.readSessionIdUseCase().stateIn(viewModelScope).value
            _accountId.value = useCases.readAccountIdUseCase().stateIn(viewModelScope).value
        }
    }

    fun getSelectedMovieTopRated(movieId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedTopRatedMovie.value = useCases.getSelectedTopRatedMovieUseCase(movieId.toInt())
        }
    }

    fun getSelectedMovieUpComing(movieId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedUpcomingMovie.value = useCases.getSelectedUpComingMovieUseCase(movieId.toInt())
        }
    }

    fun getSelectedMoviePopular(movieId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedPopularMovie.value = useCases.getSelectedPopularMovieUseCase(movieId.toInt())
        }
    }

    fun getAllGenresMovies(ids: List<Int>){
        viewModelScope.launch(Dispatchers.IO) {
            _genresMovies.value = useCases.getAllGenresMoviesUseCase(ids)
        }
    }

    fun markFavoriteTopRatedMovie(movieId: Int, favorite: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            useCases.markFavoriteTopRatedMovieUseCase(movieId, favorite)
        }
    }

    fun markFavoriteUpComingMovie(movieId: Int, favorite: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            useCases.markFavoriteUpcomingMovieUseCase(movieId, favorite)
        }
    }

    fun markFavoritePopularMovie(movieId: Int, favorite: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            useCases.markFavoritePopularMovieUseCase(movieId, favorite)
        }
    }

    fun markFavorite(accountId: String, sessionId: String, request: ApiMarkFavoriteRequest) = viewModelScope.launch {
        _markFavoriteState.update { UIState.Loading(status = true) }
        val response = useCases.markFavoriteMovieUseCase(accountId, sessionId, request)
        _markFavoriteState.update { UIState.Loading(status = false) }
        when (response) {
            is Result.Success -> _markFavoriteState.update { UIState.Success(response.data) }
            is Result.Error -> _markFavoriteState.update { UIState.Error(response.error) }
        }
        _markFavoriteState.update { UIState.InitialState }
    }

}