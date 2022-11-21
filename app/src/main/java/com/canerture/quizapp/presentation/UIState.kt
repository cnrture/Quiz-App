package com.canerture.quizapp.presentation

data class UIState<T>(
    val loadingState: Boolean = true,
    val data: T? = null
)