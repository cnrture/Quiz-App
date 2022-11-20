package com.canerture.quizapp.presentation.category

sealed class CategoryUIEffect {
    class ShowError(val message: String) : CategoryUIEffect()
    class ShowFullScreenError(val message: String) : CategoryUIEffect()
}