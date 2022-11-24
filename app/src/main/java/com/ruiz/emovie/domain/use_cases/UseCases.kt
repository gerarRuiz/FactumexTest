package com.ruiz.emovie.domain.use_cases

data class UseCases(
    val getAllTopRatedMoviesUseCase: GetAllTopRatedMoviesUseCase,
    val getAllUpComingMoviesUseCase: GetAllUpComingMoviesUseCase,
    val getSelectedTopRatedMovieUseCase: GetSelectedTopRatedMovieUseCase,
    val getSelectedUpComingMovieUseCase: GetSelectedUpComingMovieUseCase,
    val getAllGenresMoviesUseCase: GetAllGenresMoviesUseCase,
    val getMoviesVideosUseCase: GetMovieVideosUseCase,
    val saveRequestTokenUseCase: SaveRequestTokenUseCase,
    val readRequestTokenUseCase: ReadRequestTokenUseCase,
    val saveSessionIdUseCase: SaveSessionIdUseCase,
    val readSessionIdUseCase: ReadSessionIdUseCase,
    val getRequestTokenUseCase: GetRequestTokenUseCase,
    val getSessionIdUseCase: GetSessionIdUseCase,
    val saveExpireAtUseCase: SaveExpireAtUseCase,
    val readExpireAtUseCase: ReadExpireAtUseCase,
    val getAllPopularMoviesUseCase: GetAllPopularMoviesUseCase,
    val getSelectedPopularMovieUseCase: GetSelectedPopularMovieUseCase,
    val getAccountDataUseCase: GetAccountDataUseCase,
    val markFavoriteMovieUseCase: MarkFavoriteMovieUseCase,
    val saveAccountIdUseCase: SaveAccountIdUseCase,
    val readAccountIdUseCase: ReadAccountIdUseCase,
    val markFavoriteTopRatedMovieUseCase: MarkFavoriteTopRatedMovieUseCase,
    val markFavoriteUpcomingMovieUseCase: MarkFavoriteUpcomingMovieUseCase,
    val markFavoritePopularMovieUseCase: MarkFavoritePopularMovieUseCase,
    val getMyFavoritesMoviesUseCase: GetMyFavoritesMoviesUseCase
)
