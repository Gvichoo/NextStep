package com.tbacademy.nextstep.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, EVENT, EFFECT>(
    initialState: STATE,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> get() = _state


    private val _effects = Channel<EFFECT>(Channel.BUFFERED)
    val effects: Flow<EFFECT> = _effects.receiveAsFlow()

    abstract fun onEvent(event: EVENT)

    suspend fun emitEffect(effect: EFFECT) {
        _effects.send(effect)
    }

    protected fun updateState(editor: STATE.() -> STATE) {
        _state.value = editor(_state.value)
    }

}

fun <STATE, EVENT, EFFECT> BaseViewModel<STATE, EVENT, EFFECT>.launchEffect(effect: EFFECT) {
    viewModelScope.launch {
        emitEffect(effect)
    }
}