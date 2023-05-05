package com.canerture.quizapp.presentation.category

import com.canerture.quizapp.presentation.base.viewmodel.Effect

sealed class CategoryUIEffect : Effect {
    class GoToQuizScreen(val category: Int, val type: String) : CategoryUIEffect()
    class ShowError(val message: String) : CategoryUIEffect()
}