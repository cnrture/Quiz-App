package com.canerture.quizapp.presentation.category

import com.canerture.quizapp.presentation.base.Effect

sealed class CategoryUIEffect : Effect {
    class GoToQuizScreen(val category: Int, val type: String) : CategoryUIEffect()
    class ShowFullScreenError(val message: String) : CategoryUIEffect()
}