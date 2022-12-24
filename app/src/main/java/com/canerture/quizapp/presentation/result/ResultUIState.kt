package com.canerture.quizapp.presentation.result

import com.canerture.quizapp.presentation.base.State

data class ResultUIState(
    val loadingState: Boolean = false,
    val result: Float = 0f
) : State