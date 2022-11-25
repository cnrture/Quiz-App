package com.canerture.quizapp.delegation

import androidx.lifecycle.ViewModel
import com.canerture.quizapp.presentation.common.Effect
import com.canerture.quizapp.presentation.common.Event
import com.canerture.quizapp.presentation.common.State
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