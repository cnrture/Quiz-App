package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.presentation.common.State

data class HomeUIState(
    val loadingState: Boolean = false
) : State