package com.ruiz.emovie.presentation.fragments.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruiz.emovie.domain.model.ApiResponseAccount
import com.ruiz.emovie.domain.model.ApiResponseRequestToken
import com.ruiz.emovie.domain.model.ApiResponseSessionId
import com.ruiz.emovie.domain.model.RequestApiSessionId
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
class ProfileViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _requestToken = MutableStateFlow("")
    val requestToken: StateFlow<String> = _requestToken

    private val _expireAt = MutableStateFlow("")
    val expireAt: StateFlow<String> = _expireAt

    private val _sessionId = MutableStateFlow("")
    val sessionId: StateFlow<String> = _sessionId

    private val _requestTokenState: MutableLiveData<UIState<ApiResponseRequestToken>?> =
        MutableLiveData(null)
    val requestTokenState = _requestTokenState.asLiveData()

    private val _requestSessionIdState: MutableLiveData<UIState<ApiResponseSessionId>?> =
        MutableLiveData(null)
    val requestSessionIdState = _requestSessionIdState.asLiveData()

    private val _accountDataState: MutableLiveData<UIState<ApiResponseAccount>?> =
        MutableLiveData(null)
    val accountDatadState = _accountDataState.asLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _requestToken.value = useCases.readRequestTokenUseCase().stateIn(viewModelScope).value
            _expireAt.value = useCases.readExpireAtUseCase().stateIn(viewModelScope).value
            _sessionId.value = useCases.readSessionIdUseCase().stateIn(viewModelScope).value
        }
    }

    fun getFavoritesMovies(accountId: String, sessionId: String) = useCases.getMyFavoritesMoviesUseCase(accountId, sessionId)

    fun getTokenRequest() = viewModelScope.launch {
        _requestTokenState.update { UIState.Loading(status = true) }
        val response = useCases.getRequestTokenUseCase.invoke()
        _requestTokenState.update { UIState.Loading(status = false) }
        when (response) {
            is Result.Success -> _requestTokenState.update { UIState.Success(response.data) }
            is Result.Error -> _requestTokenState.update { UIState.Error(response.error) }
        }
        _requestTokenState.update { UIState.InitialState }
    }

    fun saveRequestToken(requestToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.saveRequestTokenUseCase(requestToken = requestToken)
        }
    }

    fun getSessionId(requestApiSessionId: RequestApiSessionId) = viewModelScope.launch {
        _requestSessionIdState.update { UIState.Loading(status = true) }
        val response = useCases.getSessionIdUseCase.invoke(requestApiSessionId)
        _requestSessionIdState.update { UIState.Loading(status = false) }
        when (response) {
            is Result.Success -> _requestSessionIdState.update { UIState.Success(response.data) }
            is Result.Error -> _requestSessionIdState.update { UIState.Error(response.error) }
        }
        _requestSessionIdState.update { UIState.InitialState }
    }

    fun saveSessionId(sessionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.saveSessionIdUseCase(sessionId = sessionId)
        }
    }

    fun saveExpireAt(expireAt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.saveExpireAtUseCase(expireAt = expireAt)
        }
    }

    fun readSessionId() {
        viewModelScope.launch {
            _sessionId.value = useCases.readSessionIdUseCase().stateIn(viewModelScope).value
        }
    }

    fun saveAccountId(accountId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.saveAccountIdUseCase(accountId = accountId)
        }
    }

    fun getAccountData(sessionId: String) = viewModelScope.launch {
        _accountDataState.update { UIState.Loading(status = true) }
        val response = useCases.getAccountDataUseCase.invoke(sessionId)
        _accountDataState.update { UIState.Loading(status = false) }
        when (response) {
            is Result.Success -> _accountDataState.update { UIState.Success(response.data) }
            is Result.Error -> _accountDataState.update { UIState.Error(response.error) }
        }
        _accountDataState.update { UIState.InitialState }
    }

}