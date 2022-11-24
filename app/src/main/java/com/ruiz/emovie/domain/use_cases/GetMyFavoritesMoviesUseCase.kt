package com.ruiz.emovie.domain.use_cases

import androidx.paging.PagingData
import com.ruiz.emovie.data.repository.Repository
import com.ruiz.emovie.domain.model.MyFavoriteMovies
import kotlinx.coroutines.flow.Flow

class GetMyFavoritesMoviesUseCase(
    private val repository: Repository
) {

    operator fun invoke(accountId: String, sessiondId: String): Flow<PagingData<MyFavoriteMovies>> {
        return repository.getAllMyFavoritesMovies(accountId, sessiondId)
    }

}