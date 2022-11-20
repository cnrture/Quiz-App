package com.canerture.quizapp.presentation

data class UIState<T>(
    val loadingState: Boolean = false,
    val data: T? = null
)