package com.ruiz.emovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruiz.emovie.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {

    private val _sessionId = MutableStateFlow("")
    val sessionId: StateFlow<String> = _sessionId

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _sessionId.value = useCases.readSessionIdUseCase().stateIn(viewModelScope).value
        }
    }

    fun readSessionId() {
        viewModelScope.launch {
            _sessionId.value = useCases.readSessionIdUseCase().stateIn(viewModelScope).value
        }
    }

}