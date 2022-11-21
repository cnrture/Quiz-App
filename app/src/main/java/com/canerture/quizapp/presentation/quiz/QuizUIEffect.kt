package com.canerture.quizapp.presentation.quiz

sealed class QuizUIEffect {
    object GoBack : QuizUIEffect()
    class ShowError(val message: String) : QuizUIEffect()
    class ShowFullScreenError(val message: String) : QuizUIEffect()
}