package com.canerture.quizapp.presentation.home

sealed class HomeUIEffect {
    class ShowError(val message: String) : HomeUIEffect()
    class ShowFullScreenError(val message: String) : HomeUIEffect()
}