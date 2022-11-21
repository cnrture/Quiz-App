package com.canerture.quizapp.presentation.quiz

sealed class QuizEvent {
    object CloseClicked : QuizEvent()
}