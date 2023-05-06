package com.canerture.quizapp.presentation.result

import com.canerture.quizapp.presentation.base.viewmodel.State

data class ResultUIState(
    val result: Int? = null,
    val lowerThanFifty: Boolean = false
) : State