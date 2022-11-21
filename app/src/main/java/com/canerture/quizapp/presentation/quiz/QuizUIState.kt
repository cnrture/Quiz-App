package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.presentation.common.State

data class QuizUIState(
    val loadingState: Boolean = false,
    val data: List<QuestionUI>? = null
) : State