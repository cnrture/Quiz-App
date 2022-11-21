package com.canerture.quizapp.delegation

import androidx.lifecycle.ViewModel
import com.canerture.quizapp.presentation.UIState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ViewModelDelegation<EFFECT, EVENT, STATE> {

    fun initViewModel(viewModel: ViewModel)

    fun setEffect(effect: EFFECT)

    fun setEvent(event: EVENT)

    fun setState(state: UIState<STATE>)

    val effect: SharedFlow<EFFECT>

    val event: SharedFlow<EVENT>

    val state: StateFlow<UIState<STATE>>
}