package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.presentation.base.State

sealed class HomeUIState : State {
    object Loading : HomeUIState()
    object TokenSuccess : HomeUIState()
}