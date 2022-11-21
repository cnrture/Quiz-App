package com.canerture.quizapp.presentation.category

sealed class CategoryUIEffect {
    class GoToQuizScreen(val category: Int, val difficulty: String, val type: String) :
        CategoryUIEffect()

    object GoBack : CategoryUIEffect()
    class ShowError(val message: String) : CategoryUIEffect()
    class ShowFullScreenError(val message: String) : CategoryUIEffect()
}