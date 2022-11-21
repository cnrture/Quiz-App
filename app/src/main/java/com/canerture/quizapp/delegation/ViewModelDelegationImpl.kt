package com.canerture.quizapp.delegation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.presentation.common.Effect
import com.canerture.quizapp.presentation.common.Event
import com.canerture.quizapp.presentation.common.State
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ViewModelDelegationImpl<EFFECT : Effect, EVENT : Event, STATE : State> :
    ViewModelDelegation<EFFECT, EVENT, STATE> {

    private lateinit var viewModel: ViewModel

    private val _effectTemp = MutableSharedFlow<EFFECT>()
    override val effect = _effectTemp.asSharedFlow()

    private val _eventTemp = MutableSharedFlow<EVENT>()
    override val event = _eventTemp.asSharedFlow()

    private val _stateTemp = MutableSharedFlow<STATE>()
    override val state = _stateTemp.asSharedFlow()

    override fun initViewModel(viewModel: ViewModel, initialState: STATE) {
        this.viewModel = viewModel
        this.viewModel.viewModelScope.launch {
            _stateTemp.emit(initialState)
        }
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