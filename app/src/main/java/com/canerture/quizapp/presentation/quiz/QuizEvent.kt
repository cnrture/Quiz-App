package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.presentation.base.viewmodel.Event

sealed class QuizEvent : Event {
    object CloseClicked : QuizEvent()
    class Answered(val answer: String) : QuizEvent()
    object NextQuestion : QuizEvent()
    object TimeIsUp : QuizEvent()
}