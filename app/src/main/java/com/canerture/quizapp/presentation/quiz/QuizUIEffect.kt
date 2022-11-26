package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.common.Effect

sealed class QuizUIEffect : Effect {
    object GoBack : QuizUIEffect()
    class ShowError(val message: String) : QuizUIEffect()
    class ShowFullScreenError(val message: String) : QuizUIEffect()
    object ResetUI : QuizUIEffect()
}