package com.canerture.quizapp.presentation.home

sealed class HomeUIEffect {
    object GoToCategoryScreen : HomeUIEffect()
    class ShowError(val message: String) : HomeUIEffect()
    class ShowFullScreenError(val message: String) : HomeUIEffect()
}