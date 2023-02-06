package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.presentation.base.State

sealed class QuizUIState : State {
    object Loading : QuizUIState()
    data class Data(
        val question: QuestionUI,
        val questionIndex: Int,
        val questionCount: Int
    ) : QuizUIState()
}