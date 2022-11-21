package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.presentation.common.Event

sealed class QuizEvent : Event {
    object CloseClicked : QuizEvent()
}