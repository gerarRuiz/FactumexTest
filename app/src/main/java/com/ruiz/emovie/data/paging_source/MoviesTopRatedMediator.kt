package com.ruiz.emovie.data.paging_source

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ruiz.emovie.data.local.MoviesDatabase
import com.ruiz.emovie.data.remote.MoviesApi
import com.ruiz.emovie.domain.model.MovieTopRated
import com.ruiz.emovie.domain.model.MovieTopRatedKeys

@ExperimentalPagingApi
class MoviesTopRatedMediator(
    private val moviesApi: MoviesApi,
    private val moviesDatabase: MoviesDatabase
) : RemoteMediator<Int, MovieTopRated>() {

    private val topRatedDao = moviesDatabase.moviesTopRatedDao()
    private val topRatedKeysDao = moviesDatabase.moviesTopRatedKeysDao()

    override suspend fun initialize(): InitializeAction {
        val currentTime = System.currentTimeMillis()
        val lastUpdated =
            topRatedKeysDao.getAllMovieTopRatedRemoteKeys()?.firstOrNull()?.lastUpdated ?: 0L
        val cacheTimeout = 5

        val diferienciaEnMinutos = (currentTime - lastUpdated) / 1000 / 60

        return if (diferienciaEnMinutos.toInt() <= cacheTimeout) {

            Log.d("RemoteMediatorTop", "UP TO DATE")
            InitializeAction.SKIP_INITIAL_REFRESH

        } else {

            Log.d("RemoteMediatorTop", "REFRESH")
            InitializeAction.LAUNCH_INITIAL_REFRESH

        }

    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieTopRated>
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

            val response = moviesApi.getTopRatedMovies(page = page)
            val endOfPagination = response.results.isNullOrEmpty()

            val prevPage = if (page == 1) null else page - 1
            val nextPage = if (endOfPagination) null else page + 1


            moviesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    topRatedDao.deleteAllMoviesTopRated()
                    topRatedKeysDao.deleteAllMoviesTopRatedRemoteKeys()
                }

                val lastUpdated = System.currentTimeMillis()
                val keys = response.results?.map { movieTopRated ->
                    MovieTopRatedKeys(
                        id = movieTopRated.id,
                        previousPage = prevPage,
                        nextPage = nextPage,
                        lastUpdated
                    )
                }
                topRatedKeysDao.addAllMoviesTopRatedRemoteKeys(keys ?: listOf())
                topRatedDao.addMoviesTopRated(response.results ?: listOf())
            }


            MediatorResult.Success(endOfPaginationReached = endOfPagination)

        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }

    }

    private suspend fun getRemoteKeysPorPrimeraVez(state: PagingState<Int, MovieTopRated>): MovieTopRatedKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { movie ->
            topRatedKeysDao.getMovieTopRatedRemoteKeys(movie.id)
        }
    }

    private suspend fun getRemoteKeysPorUltimaVez(state: PagingState<Int, MovieTopRated>): MovieTopRatedKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie ->
            topRatedKeysDao.getMovieTopRatedRemoteKeys(movie.id)
        }
    }

    private suspend fun getRemoteKeysCercanasPosicionActual(state: PagingState<Int, MovieTopRated>): MovieTopRatedKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                topRatedKeysDao.getMovieTopRatedRemoteKeys(id)
            }
        }
    }


}