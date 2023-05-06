package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.presentation.base.viewmodel.State

data class HomeUIState(
    val isLoading: Boolean = false
) : State