package com.canerture.quizapp.presentation.result

import com.canerture.quizapp.presentation.base.viewmodel.Effect

sealed class ResultUIEffect : Effect {
    object GoHome : ResultUIEffect()
}