package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.presentation.base.State

data class HomeUIState(
    val loadingState: Boolean = false,
) : State