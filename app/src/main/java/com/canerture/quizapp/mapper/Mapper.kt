package com.canerture.quizapp.mapper

import com.canerture.quizapp.data.model.question.Question
import com.canerture.quizapp.domain.model.question.QuestionUI

fun List<Question>.toQuestionUIList() = map {
    val list = mutableListOf(
        it.correctAnswer.orEmpty(),
        it.incorrectAnswers?.getOrNull(0).orEmpty(),
        it.incorrectAnswers?.getOrNull(1).orEmpty(),
        it.incorrectAnswers?.getOrNull(2).orEmpty()
    )
    QuestionUI(
        text = it.text.orEmpty(),
        correctAnswer = it.correctAnswer.orEmpty(),
        allAnswers = list
    )
}