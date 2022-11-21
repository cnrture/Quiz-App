package com.canerture.quizapp.presentation.category

import com.canerture.quizapp.presentation.common.Effect

sealed class CategoryUIEffect : Effect {
    class GoToQuizScreen(val category: Int, val difficulty: String, val type: String) :
        CategoryUIEffect()

    object GoBack : CategoryUIEffect()
    class ShowError(val message: String) : CategoryUIEffect()
    class ShowFullScreenError(val message: String) : CategoryUIEffect()
}