package com.ruiz.emovie.di

import android.content.Context
import com.ruiz.emovie.data.repository.DataStoreOperationsImpl
import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.repository.DataStoreOperations
import com.ruiz.emovie.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesDataStoreOperations(@ApplicationContext context: Context): DataStoreOperations {
        return DataStoreOperationsImpl(context = context)
    }

    @Provides
    @Singleton
    fun provideUseCases(repository: Repository): UseCases {
        return UseCases(
            getAllTopRatedMoviesUseCase = GetAllTopRatedMoviesUseCase(repository),
            getAllUpComingMoviesUseCase = GetAllUpComingMoviesUseCase(repository),
            getSelectedTopRatedMovieUseCase = GetSelectedTopRatedMovieUseCase(repository),
            getSelectedUpComingMovieUseCase = GetSelectedUpComingMovieUseCase(repository),
            getAllGenresMoviesUseCase = GetAllGenresMoviesUseCase(repository),
            getMoviesVideosUseCase = GetMovieVideosUseCase(repository),
            saveRequestTokenUseCase = SaveRequestTokenUseCase(repository),
            readRequestTokenUseCase = ReadRequestTokenUseCase(repository),
            saveSessionIdUseCase = SaveSessionIdUseCase(repository),
            readSessionIdUseCase = ReadSessionIdUseCase(repository),
            getRequestTokenUseCase = GetRequestTokenUseCase(repository),
            getSessionIdUseCase = GetSessionIdUseCase(repository),
            saveExpireAtUseCase = SaveExpireAtUseCase(repository),
            readExpireAtUseCase = ReadExpireAtUseCase(repository),
            getAllPopularMoviesUseCase = GetAllPopularMoviesUseCase(repository),
            getSelectedPopularMovieUseCase = GetSelectedPopularMovieUseCase(repository),
            getAccountDataUseCase = GetAccountDataUseCase(repository),
            markFavoriteMovieUseCase = MarkFavoriteMovieUseCase(repository),
            saveAccountIdUseCase = SaveAccountIdUseCase(repository),
            readAccountIdUseCase = ReadAccountIdUseCase(repository),
            markFavoriteTopRatedMovieUseCase = MarkFavoriteTopRatedMovieUseCase(repository),
            markFavoriteUpcomingMovieUseCase = MarkFavoriteUpcomingMovieUseCase(repository),
            markFavoritePopularMovieUseCase = MarkFavoritePopularMovieUseCase(repository),
            getMyFavoritesMoviesUseCase = GetMyFavoritesMoviesUseCase(repository)
        )
    }

}