package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.common.Event

sealed class QuizEvent : Event {
    object CloseClicked : QuizEvent()
    class AnswerClicked(val isCorrect: Boolean) : QuizEvent()
}