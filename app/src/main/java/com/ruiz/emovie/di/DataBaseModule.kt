package com.ruiz.emovie.di

import android.content.Context
import androidx.room.Room
import com.ruiz.emovie.data.local.MoviesDatabase
import com.ruiz.emovie.data.repository.LocalDataSourceImpl
import com.ruiz.emovie.domain.repository.LocalDataSource
import com.ruiz.emovie.util.constants.Constants.MOVIES_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun providesDataba(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MoviesDatabase::class.java,
        MOVIES_DATABASE
    ).build()

    @Provides
    @Singleton
    fun providesLocalDataSource(
        moviesDatabase: MoviesDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(
            moviesDatabase
        )
    }

}