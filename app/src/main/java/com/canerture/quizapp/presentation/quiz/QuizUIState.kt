package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.presentation.base.State

data class QuizUIState(
    val loadingState: Boolean = false,
    val data: QuestionUI? = null,
    val questionIndex: Int = 1,
    val questionCount: Int = 10
) : State