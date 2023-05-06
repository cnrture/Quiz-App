package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.presentation.base.viewmodel.State

data class QuizUIState(
    val isLoading: Boolean = false,
    val question: QuestionUI? = null,
    val questionIndex: Int? = null,
    val questionCount: Int? = null
) : State