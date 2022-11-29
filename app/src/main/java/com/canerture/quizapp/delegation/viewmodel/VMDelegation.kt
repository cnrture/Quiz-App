package com.canerture.quizapp.delegation.viewmodel

import androidx.lifecycle.ViewModel
import com.canerture.quizapp.presentation.base.Effect
import com.canerture.quizapp.presentation.base.Event
import com.canerture.quizapp.presentation.base.State
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface VMDelegation<EFFECT : Effect, EVENT : Event, STATE : State> {

    fun initViewModel(viewModel: ViewModel)

    fun setEffect(effect: EFFECT)

    fun setEvent(event: EVENT)

    fun setState(state: STATE)

    val effect: SharedFlow<EFFECT>

    val event: SharedFlow<EVENT>

    val state: StateFlow<STATE>
}