package com.canerture.quizapp.presentation.result

import com.canerture.quizapp.presentation.base.State

sealed class ResultUIState : State {
    object Loading : ResultUIState()
    data class Data(val result: Float, val lowerThanFifty: Boolean) : ResultUIState()
}