package com.ruiz.emovie.data.paging_source

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ruiz.emovie.data.local.MoviesDatabase
import com.ruiz.emovie.data.remote.MoviesApi
import com.ruiz.emovie.domain.model.MoviesPopular
import com.ruiz.emovie.domain.model.MoviesPopularKeys
import com.ruiz.emovie.domain.model.MyFavoriteMovies
import com.ruiz.emovie.domain.model.MyFavoriteMoviesKeys

@ExperimentalPagingApi
class MyFavoriteMoviesMediator(
    private val moviesApi: MoviesApi,
    private val moviesDatabase: MoviesDatabase,
    private val sessionId: String,
    private val accountId: String
) : RemoteMediator<Int, MyFavoriteMovies>() {

    private val favoriteMoviesDao = moviesDatabase.moviesFavoritesDao()
    private val favoriteMoviesKeysDao = moviesDatabase.moviesFavoritesKeysDao()

    override suspend fun initialize(): InitializeAction {
        val currentTime = System.currentTimeMillis()
        val lastUpdated =
            favoriteMoviesKeysDao.getAllMovieFavoriteKeys()?.firstOrNull()?.lastUpdated ?: 0L
        val cacheTimeout = 5

        val diferienciaEnMinutos = (currentTime - lastUpdated) / 1000 / 60

        return if (diferienciaEnMinutos.toInt() <= cacheTimeout) {

            Log.d("RemoteMediator", "UP TO DATE")
            InitializeAction.SKIP_INITIAL_REFRESH

        } else {

            Log.d("RemoteMediator", "REFRESH")
            InitializeAction.LAUNCH_INITIAL_REFRESH

        }

    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MyFavoriteMovies>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeysCercanasPosicionActual(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeysPorPrimeraVez(state)
                    val prevPage = remoteKeys?.previousPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeysPorUltimaVez(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }

            val response = moviesApi.getMyFavoriteMovies(account_id = accountId, page = page, sessionId = sessionId)
            val endOfPagination = response.results.isNullOrEmpty()

            val prevPage = if (page == 1) null else page - 1
            val nextPage = if (endOfPagination) null else page + 1

            moviesDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    favoriteMoviesDao.deleteAllFavoriteMovies()
                    favoriteMoviesKeysDao.deleteAllMoviesFavoriteRemoteKeys()
                }

                val lastUpdated = System.currentTimeMillis()
                val keys = response.results?.map { movieUpComing ->
                    MyFavoriteMoviesKeys(
                        id = movieUpComing.id,
                        previousPage = prevPage,
                        nextPage = nextPage,
                        lastUpdated = lastUpdated
                    )
                }

                favoriteMoviesDao.addMyFavoriteMovies(response.results ?: listOf())
                favoriteMoviesKeysDao.addAllMoviesFavoriteRemoteKeys(keys ?: listOf())

            }


            MediatorResult.Success(endOfPaginationReached = endOfPagination)

        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }

    }

    private suspend fun getRemoteKeysPorPrimeraVez(state: PagingState<Int, MyFavoriteMovies>): MyFavoriteMoviesKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { movie ->
            favoriteMoviesKeysDao.getMovieFavoriteRemoteKeys(movie.id)
        }
    }

    private suspend fun getRemoteKeysPorUltimaVez(state: PagingState<Int, MyFavoriteMovies>): MyFavoriteMoviesKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie ->
            favoriteMoviesKeysDao.getMovieFavoriteRemoteKeys(movie.id)
        }
    }

    private suspend fun getRemoteKeysCercanasPosicionActual(state: PagingState<Int, MyFavoriteMovies>): MyFavoriteMoviesKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                favoriteMoviesKeysDao.getMovieFavoriteRemoteKeys(id)
            }
        }
    }


}