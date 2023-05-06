package com.canerture.quizapp.presentation.base.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface VMDelegation<EFFECT : Effect, EVENT : Event, STATE : State> {

    fun initViewModel(viewModel: ViewModel)

    fun setEffect(effect: EFFECT)

    fun setEvent(event: EVENT)

    fun setState(state: STATE)

    fun getCurrentState(): STATE

    val effect: SharedFlow<EFFECT>

    val event: SharedFlow<EVENT>

    val state: StateFlow<STATE>
}