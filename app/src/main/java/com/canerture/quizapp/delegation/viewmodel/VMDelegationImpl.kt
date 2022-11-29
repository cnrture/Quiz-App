package com.canerture.quizapp.delegation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.presentation.base.Effect
import com.canerture.quizapp.presentation.base.Event
import com.canerture.quizapp.presentation.base.State
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VMDelegationImpl<EFFECT : Effect, EVENT : Event, STATE : State>(
    setInitialState: STATE
) : VMDelegation<EFFECT, EVENT, STATE> {

    private lateinit var viewModel: ViewModel

    private val _effectTemp = MutableSharedFlow<EFFECT>()
    override val effect = _effectTemp.asSharedFlow()

    private val _eventTemp = MutableSharedFlow<EVENT>()
    override val event = _eventTemp.asSharedFlow()

    private val _stateTemp = MutableStateFlow(setInitialState)
    override val state = _stateTemp.asStateFlow()

    override fun initViewModel(viewModel: ViewModel) {
        this.viewModel = viewModel
    }

    override fun setEffect(effect: EFFECT) {
        viewModel.viewModelScope.launch {
            _effectTemp.emit(effect)
        }
    }

    override fun setEvent(event: EVENT) {
        viewModel.viewModelScope.launch {
            _eventTemp.emit(event)
        }
    }

    override fun setState(state: STATE) {
        viewModel.viewModelScope.launch {
            _stateTemp.emit(state)
        }
    }
}