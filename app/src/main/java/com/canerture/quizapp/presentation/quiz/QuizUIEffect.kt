package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.presentation.base.Effect

sealed class QuizUIEffect : Effect {
    object GoBack : QuizUIEffect()
    class ShowError(val message: String) : QuizUIEffect()
    class ShowFullScreenError(val message: String) : QuizUIEffect()
    object CorrectAnswer : QuizUIEffect()
    class IncorrectAnswer(val correctAnswer: String) : QuizUIEffect()
    object NextQuestion : QuizUIEffect()
}