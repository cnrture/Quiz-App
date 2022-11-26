package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.common.Effect

sealed class HomeUIEffect : Effect {
    object GoToCategoryScreen : HomeUIEffect()
    class ShowError(val message: String) : HomeUIEffect()
    class ShowFullScreenError(val message: String) : HomeUIEffect()
}