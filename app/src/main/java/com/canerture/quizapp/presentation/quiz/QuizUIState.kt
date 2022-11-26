package com.canerture.quizapp.presentation.quiz

import com.canerture.quizapp.common.State
import com.canerture.quizapp.domain.model.question.QuestionUI

data class QuizUIState(
    val loadingState: Boolean = false,
    val data: QuestionUI? = null,
    val questionIndex: Int = 1,
    val questionCount: Int = 10
) : State