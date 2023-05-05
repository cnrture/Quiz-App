package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.presentation.base.viewmodel.Effect

sealed class HomeUIEffect : Effect {
    object GoToCategoryScreen : HomeUIEffect()
    class ShowError(val message: String) : HomeUIEffect()
}