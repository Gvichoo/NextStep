package com.tbacademy.nextstep.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, EVENT, EFFECT, UI_STATE>(
    initialState: STATE,
    initialUiState: UI_STATE
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> get() = _state

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<UI_STATE> get() = _uiState

    private val _effects = MutableSharedFlow<EFFECT>()
    val effects get() = _effects.asSharedFlow()

    abstract fun onEvent(event: EVENT)

    suspend fun emitEffect(effect: EFFECT) {
        _effects.emit(effect)
    }

    protected fun updateState(editor: STATE.() -> STATE) {
        _state.value = editor(_state.value)
    }

    protected fun updateUiState(editor: UI_STATE.() -> UI_STATE) {
        _uiState.value = editor(_uiState.value)
    }
}

fun <STATE, EVENT, EFFECT, UI_STATE> BaseViewModel<STATE, EVENT, EFFECT, UI_STATE>.launchEffect(effect: EFFECT) {
    viewModelScope.launch {
        emitEffect(effect)
    }
}